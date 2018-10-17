package org.fiuba.d2.dto;

public class Request {

    private RequestType type;
    private String key;
    private String value;

    public Request() {
    }

    public Request(RequestType type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
