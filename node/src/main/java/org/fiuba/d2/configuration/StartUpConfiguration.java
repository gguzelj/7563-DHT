package org.fiuba.d2.configuration;

import org.fiuba.d2.connector.Connector;
import org.fiuba.d2.model.membership.MembershipEvent;
import org.fiuba.d2.model.node.LocalNode;
import org.fiuba.d2.model.node.Seed;
import org.fiuba.d2.model.node.Token;
import org.fiuba.d2.model.ring.Ring;
import org.fiuba.d2.model.ring.RingImpl;
import org.fiuba.d2.persistence.ItemRepository;
import org.fiuba.d2.persistence.MembershipEventRepository;
import org.fiuba.d2.utils.NameGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.System.currentTimeMillis;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.fiuba.d2.model.membership.MembershipEventType.ADD;
import static org.fiuba.d2.model.node.Token.TokenBuilder.createRandom;
import static org.fiuba.d2.utils.NodeBuilder.localNode;
import static org.fiuba.d2.utils.NodeBuilder.remoteNode;

@Configuration
public class StartUpConfiguration {

    @Value("${dht.node.amount-tokens}")
    private Integer amountOfTokens;

    @Value("${dht.node.host:#{null}}")
    private String localNodeAddress;

    @Value("${server.port}")
    private Integer port;

    @Value("${dht.ring.seeds}")
    private List<String> seedsUris;

    private MembershipEventRepository membershipEventRepository;

    private ItemRepository itemRepository;

    private RestTemplate restTemplate;

    public StartUpConfiguration(MembershipEventRepository membershipEventRepository,
                                ItemRepository itemRepository,
                                RestTemplate restTemplate) {
        this.membershipEventRepository = membershipEventRepository;
        this.itemRepository = itemRepository;
        this.restTemplate = restTemplate;
    }

    @Bean
    public List<Seed> seeds() {
        return seedsUris.stream().map(uri -> new Seed(new Connector(uri, restTemplate))).collect(toList());
    }

    @Bean
    public Ring ring(List<Seed> seeds) {
        List<MembershipEvent> events = findEvents();
        Ring ring = events.isEmpty() ? buildNewRing() : buildFromHistory(new LinkedBlockingQueue<>(events));
        return ring;
    }


    private List<MembershipEvent> findEvents() {
        return membershipEventRepository.findAll().stream().sorted().collect(toList());
    }

    private Ring buildNewRing() {
        LocalNode node = createRandomNode();
        saveMembershipEvent(node);
        return new RingImpl(node);
    }

    private void saveMembershipEvent(LocalNode node) {
        MembershipEvent event = new MembershipEvent(
                currentTimeMillis(),
                ADD,
                node.getId(),
                node.getName(),
                node.getUri(),
                node.getTokens());
        membershipEventRepository.saveAndFlush(event);
    }

    private Ring buildFromHistory(Queue<MembershipEvent> events) {
        MembershipEvent firstEvent = events.poll();
        LocalNode localNode = localNode(firstEvent, itemRepository);
        Ring ring = new RingImpl(localNode);
        events.forEach(event -> ring.addNode(remoteNode(event, restTemplate), event.getTokens()));
        return ring;
    }

    private LocalNode createRandomNode() {
        List<Token> tokens = range(0, amountOfTokens).mapToObj(i -> createRandom()).collect(toList());
        return new LocalNode(UUID.randomUUID().toString(), NameGenerator.generateRandomName(), getUri(), tokens, itemRepository);
    }

    private String getUri() {
        return "http://" + localNodeAddress + ":" + port;
    }
}
