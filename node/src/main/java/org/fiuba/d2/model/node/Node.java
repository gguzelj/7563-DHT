package org.fiuba.d2.model.node;

import java.util.List;

public interface Node {

    String getId();

    String getName();

    NodeStatus getStatus();

    List<Token> getTokens();

    void put(String key, String value);

    String get(String key);

    String getUri();

    NodeInfo getInfo();
}
