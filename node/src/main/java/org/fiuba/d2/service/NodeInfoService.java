package org.fiuba.d2.service;

import org.fiuba.d2.model.node.Node;
import org.fiuba.d2.model.node.NodeInfo;
import org.fiuba.d2.model.node.NodeStatus;
import org.fiuba.d2.model.ring.Ring;
import org.fiuba.d2.persistence.MembershipEventRepository;
import org.springframework.stereotype.Component;

import static org.fiuba.d2.utils.HashGenerator.sha1;

@Component
public class NodeInfoService {

    private final Ring ring;

    public NodeInfoService(Ring ring) {
        this.ring = ring;
    }

    public NodeInfo getHostInfo() {
        Node localNode = ring.getLocalNode();
        return new NodeInfo(localNode.getId(), localNode.getName(), NodeStatus.AVAILABLE, localNode.getTokens());
    }

}
