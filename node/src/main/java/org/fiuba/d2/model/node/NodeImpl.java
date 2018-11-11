package org.fiuba.d2.model.node;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public abstract class NodeImpl implements Node {

    protected String id;
    protected String name;
    protected String uri;
    protected List<Token> tokens;

    public NodeImpl() {
    }

    public NodeImpl(String id, String name, String uri, List<Token> tokens) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.tokens = tokens;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public List<Token> getTokens() {
        return tokens.stream().sorted().collect(toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeImpl node = (NodeImpl) o;
        return Objects.equals(id, node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
