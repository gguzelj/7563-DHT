package org.fiuba.d2.utils;

import org.fiuba.d2.connector.Connector;
import org.fiuba.d2.model.membership.MembershipEvent;
import org.fiuba.d2.model.node.LocalNode;
import org.fiuba.d2.model.node.RemoteNode;
import org.fiuba.d2.persistence.ItemRepository;
import org.springframework.web.client.RestTemplate;

public class NodeBuilder {

    public static RemoteNode remoteNode(MembershipEvent event, RestTemplate restTemplate) {
        Connector connector = new Connector(event.getUri(), restTemplate);
        return new RemoteNode(event.getNodeId(), event.getName(), event.getUri(), event.getTokens(), connector);
    }
}
