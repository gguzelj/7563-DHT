package org.fiuba.d2.persistence.local;

import javax.persistence.*;
import java.util.List;

@Entity
public class PersistedNode {

    @Id
    @GeneratedValue
    private Integer id;
    private String nodeId;
    private String nodeName;
    private String uri;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<LocalToken> tokens;

    public PersistedNode() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<LocalToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<LocalToken> tokens) {
        this.tokens = tokens;
    }
}
