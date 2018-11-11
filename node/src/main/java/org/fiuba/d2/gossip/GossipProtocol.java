package org.fiuba.d2.gossip;

import org.fiuba.d2.model.node.Node;
import org.fiuba.d2.model.node.NodeInfo;
import org.fiuba.d2.model.ring.Ring;
import org.fiuba.d2.persistence.MembershipEventRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

public class GossipProtocol {

    private final Ring ring;
    private final MembershipEventRepository membershipEventRepository;
    private final RestTemplate restTemplate;

    @Value("${server.port")
    private Integer port;

    public GossipProtocol(Ring ring, MembershipEventRepository membershipEventRepository, RestTemplate restTemplate) {
        this.ring = ring;
        this.membershipEventRepository = membershipEventRepository;
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 1000, fixedDelay = 2000)
    public void checkForNewEvents() {
        List<Node> nodes = ring.getNodes();
        Node node = nodes.get(new Random().nextInt(nodes.size()));

        ResponseEntity<NodeInfo> response = restTemplate.getForEntity(node.getUri(), NodeInfo.class);


    }

    public void reconcile(Ring ring) {

    }
}
