package org.fiuba.d2.service;

import org.fiuba.d2.model.membership.MembershipEvent;
import org.fiuba.d2.model.node.*;
import org.fiuba.d2.model.ring.Ring;
import org.fiuba.d2.model.ring.RingImpl;
import org.fiuba.d2.persistence.ItemRepository;
import org.fiuba.d2.persistence.MembershipEventRepository;
import org.fiuba.d2.persistence.local.LocalToken;
import org.fiuba.d2.persistence.local.PersistedNode;
import org.fiuba.d2.persistence.local.PersistedNodeRepository;
import org.fiuba.d2.utils.NameGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.System.currentTimeMillis;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.fiuba.d2.model.membership.MembershipEventType.ADD;
import static org.fiuba.d2.model.node.Token.TokenBuilder.createRandom;
import static org.fiuba.d2.utils.NodeBuilder.remoteNode;

@Service
public class RingService {

    @Value("${dht.node.amount-tokens}")
    private Integer amountOfTokens;

    @Value("${dht.node.host:#{null}}")
    private String localNodeAddress;

    @Value("${server.port}")
    private Integer port;

    private MembershipEventRepository membershipEventRepository;
    private PersistedNodeRepository persistedNodeRepository;
    private ItemRepository itemRepository;
    private RestTemplate restTemplate;
    private List<Seed> seeds;
    private Ring ring;

    public RingService(MembershipEventRepository membershipEventRepository,
                       PersistedNodeRepository persistedNodeRepository,
                       ItemRepository itemRepository,
                       RestTemplate restTemplate, List<Seed> seeds) {
        this.membershipEventRepository = membershipEventRepository;
        this.persistedNodeRepository = persistedNodeRepository;
        this.itemRepository = itemRepository;
        this.restTemplate = restTemplate;
        this.seeds = seeds;
    }

    @PostConstruct
    private void initializeRing(){
        List<MembershipEvent> events = findEvents();
        this.ring = events.isEmpty() ? buildNewRing(seeds) : buildFromHistory(seeds, events);
    }


    private List<MembershipEvent> findEvents() {
        return membershipEventRepository.findAll().stream().sorted().collect(toList());
    }

    private Ring buildNewRing(List<Seed> seeds) {
        LocalNode node = createRandomNode();
        MembershipEvent event = buildEvent(node);
        saveLocalNode(node);
        saveMembershipEvent(event);
        notifySeeds(seeds, event);
        RingImpl ring = new RingImpl(node);
        readAllEvents(seeds).stream()
                .filter(e -> !e.getNodeId().equals(node.getId()))
                .forEach(e -> {
                    membershipEventRepository.saveAndFlush(e);
                    ring.addNode(remoteNode(e, restTemplate), e.getTokens());
                });
        return ring;
    }

    private List<MembershipEvent> readAllEvents(List<Seed> seeds) {
        return seeds.isEmpty() ? new ArrayList<>() : seeds.get(0).getAllEvents();
    }

    private List<MembershipEvent> readAllEventsSince(List<Seed> seeds, Long timestamp) {
        return seeds.isEmpty() ? new ArrayList<>() : seeds.get(0).getEventsSince(timestamp);
    }

    private void notifySeeds(List<Seed> seeds, MembershipEvent event) {
        seeds.forEach(seed -> seed.sendEvent(event));
    }

    private void saveLocalNode(LocalNode node) {
        PersistedNode persistedNode = new PersistedNode();
        persistedNode.setNodeId(node.getId());
        persistedNode.setNodeName(node.getName());
        persistedNode.setUri(node.getUri());
        persistedNode.setTokens(node.getTokens().stream().map(t -> new LocalToken(t.getValue())).collect(toList()));
        persistedNodeRepository.save(persistedNode);
    }

    private LocalNode findLocalNode() {
        PersistedNode node = persistedNodeRepository.findAll().get(0);
        List<Token> tokens = node.getTokens().stream().map(t -> new Token(t.getValue())).collect(toList());
        return new LocalNode(node.getNodeId(), node.getNodeName(), node.getUri(), tokens, itemRepository);
    }

    private void saveMembershipEvent(MembershipEvent event) {
        membershipEventRepository.saveAndFlush(event);
    }

    private MembershipEvent buildEvent(LocalNode node) {
        return new MembershipEvent(currentTimeMillis(),ADD,node.getId(),node.getName(),node.getUri(),node.getTokens());
    }

    private Ring buildFromHistory(List<Seed> seeds, List<MembershipEvent> events) {
        LocalNode localNode = findLocalNode();
        Ring ring = new RingImpl(localNode);
        events.stream().filter(e -> !e.getNodeId().equals(localNode.getId()))
                .forEach(event -> ring.addNode(remoteNode(event, restTemplate), event.getTokens()));

        MembershipEvent lastEvent = events.get(events.size() - 1);
        List<MembershipEvent> membershipEvents = readAllEventsSince(seeds, lastEvent.getTimestamp());
        membershipEvents.forEach(event -> {
            membershipEventRepository.saveAndFlush(event);
            ring.addNode(remoteNode(event, restTemplate), event.getTokens());
        });

        return ring;
    }

    private LocalNode createRandomNode() {
        List<Token> tokens = range(0, amountOfTokens).mapToObj(i -> createRandom()).collect(toList());
        return new LocalNode(UUID.randomUUID().toString(), NameGenerator.generateRandomName(), getUri(), tokens, itemRepository);
    }

    private String getUri() {
        return "http://" + localNodeAddress + ":" + port;
    }

    public Node getCoordinatorNode(Token token) {
        return ring.getCoordinatorNode(token);
    }

    public void addNode(Node node, List<Token> tokens) {
        ring.addNode(node, tokens);
    }

    public void removeNode(Node node) {
        ring.removeNode(node);
    }

    public List<Node> getNodes() {
        return ring.getNodes();
    }
}
