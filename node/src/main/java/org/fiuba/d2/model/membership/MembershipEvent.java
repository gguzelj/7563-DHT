package org.fiuba.d2.model.membership;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.fiuba.d2.model.node.Node;
import org.fiuba.d2.model.node.Token;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

import static java.lang.System.currentTimeMillis;
import static java.util.Objects.isNull;

@Entity
public class MembershipEvent implements Comparable<MembershipEvent> {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private Long timestamp;
    private MembershipEventType type;
    private String nodeId;
    private String name;
    private String uri;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Token> tokens;

    private MembershipEvent() {
    }

    public MembershipEvent(Long timestamp, MembershipEventType type, Node node) {
        this(timestamp, type, node.getId(), node.getName(), node.getUri(), node.getTokens());
    }

    public MembershipEvent(Long timestamp, MembershipEventType type, String nodeId, String name, String uri, List<Token> tokens) {
        this.timestamp = isNull(timestamp) ? currentTimeMillis() : timestamp;
        this.type = type;
        this.nodeId = nodeId;
        this.name = name;
        this.uri = uri;
        this.tokens = tokens;
    }

    public Integer getId() {
        return id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public MembershipEventType getType() {
        return type;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    @Override
    public int compareTo(MembershipEvent o) {
        return timestamp.compareTo(o.getTimestamp());
    }


    @Override
    public String toString() {
        return "MembershipEvent{" +
                "timestamp=" + timestamp +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MembershipEvent that = (MembershipEvent) o;
        return Objects.equals(timestamp, that.timestamp) &&
                type == that.type &&
                Objects.equals(nodeId, that.nodeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, type, nodeId);
    }
}
