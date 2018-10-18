package org.fiuba.d2.model;

public class Range {

    private final Token from;
    private final Token to;
    private final Node node;

    public Range(Token from, Token to, Node node) {
        this.from = from;
        this.to = to;
        this.node = node;
    }

    public Token getFrom() {
        return from;
    }

    public Token getTo() {
        return to;
    }

    public Node getNode() {
        return node;
    }

    public Boolean contains(Token token) {
        return from.getValue().compareTo(token.getValue()) < 0 &&
                to.getValue().compareTo(token.getValue()) >= 0;
    }

}
