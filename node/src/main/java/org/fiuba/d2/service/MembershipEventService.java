package org.fiuba.d2.service;

import org.fiuba.d2.connector.Connector;
import org.fiuba.d2.model.membership.MembershipEvent;
import org.fiuba.d2.model.node.Node;
import org.fiuba.d2.model.node.NodeStatus;
import org.fiuba.d2.model.node.RemoteNode;
import org.fiuba.d2.model.ring.Ring;
import org.fiuba.d2.persistence.MembershipEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.util.Objects.nonNull;

@Service
public class MembershipEventService {

    private MembershipEventRepository repository;
    private RestTemplate restTemplate;
    private Ring ring;

    public MembershipEventService(MembershipEventRepository repository, RestTemplate restTemplate, Ring ring) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.ring = ring;
    }

    public List<MembershipEvent> getEventsSince(Long timestamp) {
        return nonNull(timestamp) ? repository.findByTimestampAfter(timestamp) : repository.findAll();
    }

    public void addNewEvent(MembershipEvent event) {
        repository.saveAndFlush(event);
        Node node = createNode(event);
        ring.addNode(node, node.getTokens());
    }

    private Node createNode(MembershipEvent event) {
        Connector connector = new Connector(event.getUri(), restTemplate);
        return new RemoteNode(event.getNodeId(), event.getName(), event.getUri(), event.getTokens(), connector);
    }
}
