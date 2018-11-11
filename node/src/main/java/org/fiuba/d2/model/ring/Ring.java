package org.fiuba.d2.model.ring;

import org.fiuba.d2.model.node.Token;
import org.fiuba.d2.model.node.Node;

import java.util.List;

public interface Ring {

    void addNode(Node node, List<Token> tokens);

    void removeNode(Node node);

    Node getCoordinatorNode(Token token);

    List<Node> getNodes();

    Node getLocalNode();

}
