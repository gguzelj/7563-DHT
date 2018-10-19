package org.fiuba.d2.model.membership;

import org.fiuba.d2.model.Token;

import javax.persistence.*;
import java.util.List;

@Entity
public class MembershipEvent implements Comparable<MembershipEvent> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private Long timestamp;
    private MembershipEventType type;
    private String uri;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Token> tokens;

    private MembershipEvent() {
    }

    public MembershipEvent(Long timestamp, MembershipEventType type, String uri, List<Token> tokens) {
        this.timestamp = timestamp;
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

    public MembershipEventType getType() {
        return type;
    }

    public void setType(MembershipEventType type) {
        this.type = type;
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

    @Override
    public int compareTo(MembershipEvent o) {
        return timestamp.compareTo(o.getTimestamp());
    }
}
