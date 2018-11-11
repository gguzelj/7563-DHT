package org.fiuba.d2.model.node;

import java.util.List;

public class NodeInfo {

    private String id;
    private String name;
    private NodeStatus status;
    private List<Token> tokens;

    public NodeInfo() {
    }

    public NodeInfo(String id, String name, NodeStatus status, List<Token> tokens) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.tokens = tokens;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NodeStatus getStatus() {
        return status;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

}
