package org.fiuba.d2.gossip;

import org.fiuba.d2.connector.Connector;
import org.fiuba.d2.model.membership.MembershipEvent;
import org.fiuba.d2.model.membership.MembershipEventType;
import org.fiuba.d2.model.node.Node;
import org.fiuba.d2.model.ring.Ring;
import org.fiuba.d2.persistence.MembershipEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

import static java.lang.System.currentTimeMillis;
import static org.fiuba.d2.utils.NodeBuilder.remoteNode;

@Component
public class GossipProtocol {

    private static final Logger LOG = LoggerFactory.getLogger(GossipProtocol.class);

    private final Ring ring;
    private final MembershipEventRepository membershipEventRepository;
    private final RestTemplate restTemplate;

    public GossipProtocol(Ring ring, MembershipEventRepository membershipEventRepository, RestTemplate restTemplate) {
        this.ring = ring;
        this.membershipEventRepository = membershipEventRepository;
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 1000, initialDelay=2000)
    public void checkForNewEvents() {
        Connector connector = findRandomConnector();
        MembershipEvent lastEvent = membershipEventRepository.findTopByOrderByTimestampDesc();
        List<MembershipEvent> eventsSince = connector.getEventsSince(lastEvent.getTimestamp() + 1);
        eventsSince.forEach(event -> {
            LOG.info("Updating ring with event {}", event);
            membershipEventRepository.saveAndFlush(event);
            if (event.getType().equals(MembershipEventType.ADD)) {
                ring.addNode(remoteNode(event, restTemplate), event.getTokens());
            } else {
                ring.removeNode(remoteNode(event, restTemplate));
            }
        });

    }

    private Connector findRandomConnector() {
        List<Node> nodes = ring.getNodes();
        Node node = nodes.get(new Random().nextInt(nodes.size()));
        return new Connector(node.getUri(), restTemplate);
    }

}
