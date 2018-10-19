package org.fiuba.d2.model;

import org.fiuba.d2.persistence.ItemRepository;

import java.util.List;

public class LocalNode implements Node {

    private final ItemRepository repository;

    public LocalNode(ItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public NodeStatus getStatus() {
        return NodeStatus.AVAILABLE;
    }

    @Override
    public List<Token> getTokens() {
        return null;
    }

    @Override
    public void put(String key, String value) {

    }

    @Override
    public String get(String key) {
        return "";
    }
}
