package org.fiuba.d2.persistence.local;

import javax.persistence.Embeddable;

@Embeddable
public class LocalToken {

    private String value;

    public LocalToken() {
    }

    public LocalToken(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
