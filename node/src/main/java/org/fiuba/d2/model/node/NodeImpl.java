package org.fiuba.d2.model.node;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity
@DiscriminatorColumn(name="TYPE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class NodeImpl implements Node {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    protected String id;
    protected String name;
    protected String uri;
    protected NodeStatus status;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    protected List<Token> tokens;

    public NodeImpl() {
    }

    public NodeImpl(String name, String uri, NodeStatus status, List<Token> tokens) {
        this.name = name;
        this.uri = uri;
        this.status = status;
        this.tokens = tokens;
    }

    @Override
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
        return tokens.stream().sorted().collect(toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeImpl node = (NodeImpl) o;
        return Objects.equals(id, node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
