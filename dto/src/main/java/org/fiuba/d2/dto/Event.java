package org.fiuba.d2.dto;

import java.util.List;

public class Event {

    private Long timestamp;
    private EventType type;
    private String id;
    private String name;
    private String uri;
    private List<Token> tokens;

    public Event(Long timestamp, EventType type, String id, String name, String uri, List<Token> tokens) {
        this.timestamp = timestamp;
        this.id = id;
        this.name = name;
        this.type = type;
        this.uri = uri;
        this.tokens = tokens;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }
}
