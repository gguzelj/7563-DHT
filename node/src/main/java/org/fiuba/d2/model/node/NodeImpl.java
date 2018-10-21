package org.fiuba.d2.model.node;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorColumn(name="TYPE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class NodeImpl implements Node {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private String name;
    private String uri;
    private NodeStatus status;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Token> tokens;

    public NodeImpl() {
    }

    public NodeImpl(String name, String uri, NodeStatus status, List<Token> tokens) {
        this.name = name;
        this.uri = uri;
        this.status = status;
        this.tokens = tokens;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public NodeStatus getStatus() {
        return status;
    }

    @Override
    public List<Token> getTokens() {
        return tokens;
    }

}
