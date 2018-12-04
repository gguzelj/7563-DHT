package org.fiuba.d2.model.node;

import java.util.List;

public interface Node {

    String getId();

    String getName();

    List<Token> getTokens();

    void put(String key, String value);

    String get(String key);

    String getUri();

}
