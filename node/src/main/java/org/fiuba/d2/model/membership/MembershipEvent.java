package org.fiuba.d2.model.membership;

import org.fiuba.d2.model.node.Node;
import org.fiuba.d2.model.node.NodeImpl;

import javax.persistence.*;

@Entity
public class MembershipEvent implements Comparable<MembershipEvent> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private Long timestamp;
    private MembershipEventType type;

    @OneToOne(cascade = CascadeType.ALL)
    private NodeImpl node;

    private MembershipEvent() {
    }

    public MembershipEvent(Long timestamp, MembershipEventType type, NodeImpl node) {
        this.timestamp = timestamp;
        this.type = type;
        this.node = node;
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

    public Node getNode() {
        return node;
    }

    public void setNode(NodeImpl node) {
        this.node = node;
    }

    @Override
    public int compareTo(MembershipEvent o) {
        return timestamp.compareTo(o.getTimestamp());
    }
}
