package org.fiuba.d2.configuration;

import org.fiuba.d2.model.membership.MembershipEvent;
import org.fiuba.d2.model.membership.MembershipEventType;
import org.fiuba.d2.model.node.LocalNode;
import org.fiuba.d2.model.node.Token;
import org.fiuba.d2.model.ring.Ring;
import org.fiuba.d2.model.ring.RingImpl;
import org.fiuba.d2.persistence.ItemRepository;
import org.fiuba.d2.persistence.MembershipEventRepository;
import org.fiuba.d2.utils.NameGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.System.currentTimeMillis;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.fiuba.d2.model.membership.MembershipEventType.*;
import static org.fiuba.d2.model.node.Token.TokenBuilder.createRandom;

@Configuration
public class StartUpConfiguration {

    @Value("${dht.node.amount-tokens}")
    private Integer amountOfTokens;
    private MembershipEventRepository membershipEventRepository;
    private ItemRepository itemRepository;

    public StartUpConfiguration(MembershipEventRepository membershipEventRepository, ItemRepository itemRepository) {
        this.membershipEventRepository = membershipEventRepository;
        this.itemRepository = itemRepository;
    }

    @Bean
    public Ring ring() {
        List<MembershipEvent> events = membershipEventRepository.findAll();
        return events.isEmpty() ? buildNewRing() : buildFromHistory(events);
    }

    private Ring buildNewRing() {
        LocalNode randomNode = createRandomNode();
        membershipEventRepository.saveAndFlush(new MembershipEvent(currentTimeMillis(), ADD, randomNode));
        return new RingImpl(randomNode);
    }

    private Ring buildFromHistory(List<MembershipEvent> events) {
        return buildFromHistory(new LinkedBlockingQueue<>(events.stream().sorted().collect(toList())));
    }

    private Ring buildFromHistory(Queue<MembershipEvent> events) {
        Ring ring = new RingImpl(events.poll().getNode());
        events.forEach(event -> ring.addNode(event.getNode(), event.getNode().getTokens()));
        return ring;
    }

    private LocalNode createRandomNode() {
        List<Token> tokens = new ArrayList<>();
        range(0, amountOfTokens).forEach(i -> tokens.add(createRandom()));
        //TODO check null uri
        return new LocalNode(NameGenerator.generateRandomName(), null, tokens, itemRepository);
    }
}
