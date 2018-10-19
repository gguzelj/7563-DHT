package org.fiuba.d2.model;

import java.util.List;

public interface Node {

    NodeStatus getStatus();

    List<Token> getTokens();

    void put(String key, String value);

    String get(String key);

}
