package org.fiuba.d2.model.node;

public enum NodeType {

    LOCAL("LOCAL"), REMOTE("REMOTE");

    private String value;

    NodeType(final String aValue) {
        this.value = aValue;
    }
}
