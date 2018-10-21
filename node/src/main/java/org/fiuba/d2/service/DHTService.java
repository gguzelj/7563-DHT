package org.fiuba.d2.service;

import org.fiuba.d2.model.node.Node;
import org.fiuba.d2.model.node.Token;
import org.fiuba.d2.model.ring.Ring;
import org.springframework.stereotype.Component;

import static org.fiuba.d2.utils.HashGenerator.sha1;

@Component
public class DHTService {

    private final Ring ring;

    public DHTService(Ring ring) {
        this.ring = ring;
    }

    public void put(String key, String value) {
        Node coordinatorNode = ring.getCoordinatorNode(Token.TokenBuilder.withValue(sha1(key)));
        coordinatorNode.put(key, value);
    }

    public String get(String key) {
        Node coordinatorNode = ring.getCoordinatorNode(Token.TokenBuilder.withValue(sha1(key)));
        return coordinatorNode.get(key);
    }

}
