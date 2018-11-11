package org.fiuba.d2.configuration;

import org.fiuba.d2.model.node.Seed;
import org.fiuba.d2.model.membership.MembershipEvent;
import org.fiuba.d2.model.node.*;
import org.fiuba.d2.model.ring.Ring;
import org.fiuba.d2.model.ring.RingImpl;
import org.fiuba.d2.persistence.ItemRepository;
import org.fiuba.d2.persistence.MembershipEventRepository;
import org.fiuba.d2.utils.IpChecker;
import org.fiuba.d2.utils.NameGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.System.currentTimeMillis;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.fiuba.d2.model.membership.MembershipEventType.*;
import static org.fiuba.d2.model.node.Token.TokenBuilder.createRandom;

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

    public StartUpConfiguration(MembershipEventRepository membershipEventRepository, ItemRepository itemRepository, RestTemplate restTemplate) {
        this.membershipEventRepository = membershipEventRepository;
        this.itemRepository = itemRepository;
        this.restTemplate = restTemplate;
    }

    @Bean
    public List<Seed> seeds() {
        return seedsUris.stream().map(uri -> new Seed(uri, restTemplate)).collect(toList());
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
        LocalNode randomNode = createRandomNode();
        membershipEventRepository.saveAndFlush(new MembershipEvent(currentTimeMillis(), ADD, randomNode));
        return new RingImpl(randomNode);
    }

    private Ring buildFromHistory(Queue<MembershipEvent> events) {
        Ring ring = new RingImpl(events.poll().getNode());
        events.forEach(event -> ring.addNode(event.getNode(), event.getNode().getTokens()));
        return ring;
    }

    private LocalNode createRandomNode() {
        List<Token> tokens = range(0, amountOfTokens).mapToObj(i -> createRandom()).collect(toList());
        return new LocalNode(NameGenerator.generateRandomName(), getUri(), tokens, itemRepository);
    }

    private String getUri() {
        String address = isNull(localNodeAddress) ? IpChecker.getIp() : localNodeAddress;
        return address + ":" + port;
    }
}
