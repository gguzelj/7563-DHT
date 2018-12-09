package org.fiuba.d2.service;

import org.fiuba.d2.connector.Connector;
import org.fiuba.d2.model.Item;
import org.fiuba.d2.model.exception.UnreachableNodeException;
import org.fiuba.d2.model.membership.MembershipEvent;
import org.fiuba.d2.model.node.*;
import org.fiuba.d2.model.ring.Range;
import org.fiuba.d2.model.ring.Ring;
import org.fiuba.d2.model.ring.RingImpl;
import org.fiuba.d2.persistence.ItemRepository;
import org.fiuba.d2.utils.RetriableTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.fiuba.d2.model.membership.MembershipEventType.ADD;
import static org.fiuba.d2.model.node.Token.TokenBuilder.createRandom;
import static org.fiuba.d2.utils.NameGenerator.generateRandomName;
import static org.fiuba.d2.utils.NodeBuilder.remoteNode;
import static org.fiuba.d2.utils.RetriableTask.*;

@Service
public class RingService {

    private static final Logger LOG = LoggerFactory.getLogger(RingService.class);

    @Value("${dht.node.amount-tokens}")
    private Integer amountOfTokens;

    @Value("${dht.node.host:#{null}}")
    private String localNodeAddress;

    @Value("${server.port}")
    private Integer port;

    private EventService eventService;
    private LocalNodeService localNodeService;
    private ItemRepository itemRepository;
    private RestTemplate restTemplate;
    private List<Seed> seeds;
    private Ring ring;

    public RingService(EventService eventService,
                       LocalNodeService localNodeService,
                       ItemRepository itemRepository,
                       RestTemplate restTemplate, List<Seed> seeds) {
        this.eventService = eventService;
        this.localNodeService = localNodeService;
        this.itemRepository = itemRepository;
        this.restTemplate = restTemplate;
        this.seeds = seeds;
    }

    @PostConstruct
    private void initializeRing(){
        List<MembershipEvent> events = eventService.findAll();
        this.ring = events.isEmpty() ? buildNewRing() : buildFromHistory(events);
    }

    private Ring buildNewRing() {
        LocalNode localNode = createRandomNode();
        Ring ring = buildRing(localNode);
        MembershipEvent event = new MembershipEvent(ADD, localNode);
        eventService.addNewEvent(event);
        runAsync(() -> seeds.forEach(seed -> submit(() -> seed.sendEvent(event))));
        runAsync(() -> {
            List<MembershipEvent> events = readAllEventsSince(seeds, 0L);
            events.forEach(e -> {
                eventService.addNewEvent(e);
                updateRing(e);
            });
        });
        return ring;
    }

    private Ring buildFromHistory(List<MembershipEvent> events) {
        LocalNode localNode = localNodeService.findLocalNode();
        List<MembershipEvent> filteredEvents= events.stream().filter(e -> !e.getNodeId().equals(localNode.getId()))
                .collect(toList());
        Ring ring = buildRing(localNode, filteredEvents);
        runAsync(() -> {
            List<MembershipEvent> lostEvents = searchLostEvents(events);
            lostEvents.forEach(e -> {
                eventService.addNewEvent(e);
                updateRing(e);
            });
        });
        return ring;
    }

    private List<MembershipEvent> searchLostEvents(List<MembershipEvent> knownEvents) {
        MembershipEvent lastEvent = knownEvents.get(knownEvents.size() - 1);
        return readAllEventsSince(seeds, lastEvent.getTimestamp());
    }

    private Ring buildRing(LocalNode localNode) {
        return buildRing(localNode, new ArrayList<>());
    }

    private Ring buildRing(LocalNode localNode, List<MembershipEvent> events) {
        Ring ring = new RingImpl(localNode);
        events.removeIf(e -> e.getNodeId().equals(localNode.getId()));
        events.forEach(event -> ring.addNode(remoteNode(event, restTemplate)));
        return ring;
    }

    private List<MembershipEvent> readAllEventsSince(List<Seed> seeds, Long timestamp) {
        if (seeds.isEmpty())
            return new ArrayList<>();
        while (true) {
            for (Seed seed : seeds) {
                try {
                    return seed.getEventsSince(timestamp);
                } catch (UnreachableNodeException e) {
                    sleepOneSecond();
                }
            }
        }
    }

    private LocalNode createRandomNode() {
        List<Token> tokens = range(0, amountOfTokens).mapToObj(i -> createRandom()).collect(toList());
        LocalNode node = new LocalNode(randomUUID().toString(), generateRandomName(), getUri(), tokens, itemRepository);
        localNodeService.saveLocalNode(node);
        return node;
    }

    private String getUri() {
        return "http://" + localNodeAddress + ":" + port;
    }

    public Node getCoordinatorNode(Token token) {
        return ring.getCoordinatorNode(token);
    }

    public void addNode(Node node) {
        ring.addNode(node);
        List<Range> ranges = ring.getRanges(node);
        for (Range range : ranges) {
            migrate(range.getFrom(), range.getTo(), range.getNode());
        }
    }

    private void migrate(Token from, Token to, Node node) {
        List<Item> items = itemRepository.findItemsByIdBetween(from.getValue(), to.getValue());
        items.forEach(item -> {
        LOG.info("Migrating keys to node to {}[{}]", node.getName(), node.getUri());
            submit(() -> {
                node.put(item .getKey(), item.getValue());
                itemRepository.delete(item);
            });
        });
    }

    public void removeNode(Node node) {
        ring.removeNode(node);
    }

    public Ring getRing() {
        return ring;
    }

    //TODO consider REMOVE type event
    public void updateRing(MembershipEvent membershipEvent) {
        addNode(createNode(membershipEvent));
    }

    private Node createNode(MembershipEvent event) {
        Connector connector = new Connector(event.getUri(), restTemplate);
        return new RemoteNode(event.getNodeId(), event.getName(), event.getUri(), event.getTokens(), connector);
    }

}
