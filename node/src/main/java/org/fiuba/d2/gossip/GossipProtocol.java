package org.fiuba.d2.gossip;

import org.fiuba.d2.connector.Connector;
import org.fiuba.d2.model.exception.UnreachableNodeException;
import org.fiuba.d2.model.membership.MembershipEvent;
import org.fiuba.d2.model.membership.MembershipEventType;
import org.fiuba.d2.model.node.Node;
import org.fiuba.d2.model.ring.Ring;
import org.fiuba.d2.persistence.MembershipEventRepository;
import org.fiuba.d2.service.RingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.lang.System.exit;
import static java.lang.Thread.sleep;
import static java.util.stream.Collectors.toList;
import static org.fiuba.d2.utils.NodeBuilder.remoteNode;

@Component
public class GossipProtocol {

    private static final Logger LOG = LoggerFactory.getLogger(GossipProtocol.class);

    private final RingService ringService;
    private final MembershipEventRepository membershipEventRepository;
    private final RestTemplate restTemplate;

    public GossipProtocol(RingService ringService, MembershipEventRepository membershipEventRepository, RestTemplate restTemplate) {
        this.ringService = ringService;
        this.membershipEventRepository = membershipEventRepository;
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 1000, initialDelay = 2000)
    public void checkForNewEvents() {
        findRandomConnector().ifPresent(connector -> {
            MembershipEvent lastEvent = membershipEventRepository.findTopByOrderByTimestampDesc();
            List<MembershipEvent> eventsSince = getEvents(connector, lastEvent);
            eventsSince.forEach(event -> {
                LOG.info("Updating ring with event {}", event);
                membershipEventRepository.saveAndFlush(event);
                if (event.getType().equals(MembershipEventType.ADD)) {
                    ringService.addNode(remoteNode(event, restTemplate));
                } else {
                    ringService.removeNode(remoteNode(event, restTemplate));
                }
            });
        });
    }

    private List<MembershipEvent> getEvents(Connector connector, MembershipEvent lastEvent) {
        try {
            return connector.getEventsSince(lastEvent.getTimestamp() + 1);
        } catch (UnreachableNodeException e) {
            return new ArrayList<>();
        }
    }

    private Optional<Connector> findRandomConnector() {
        Ring ring = ringService.getRing();
        Node localNode = ring.getLocalNode();
        List<Node> nodes = ring.getNodes().stream().filter(n -> !n.equals(localNode)).collect(toList());
        if (nodes.isEmpty()) {
            return Optional.empty();
        }
        Node node = nodes.get(new Random().nextInt(nodes.size()));
        return Optional.of(new Connector(node.getUri(), restTemplate));
    }

}
