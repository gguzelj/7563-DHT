package org.fiuba.d2.model.node;

import org.fiuba.d2.connector.Connector;
import org.fiuba.d2.dto.Request;
import org.fiuba.d2.dto.RequestType;
import org.fiuba.d2.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.List;

public class RemoteNode extends NodeImpl {

    private final Logger LOG = LoggerFactory.getLogger(RemoteNode.class);

    private Connector connector;

    public RemoteNode(String id, String name, String uri, List<Token> tokens, Connector connector) {
        super(id, name, uri, tokens);
        this.connector = connector;
    }

    @Override
    public void put(String key, String value) {
        LOG.info("Forwarding put request \"{}\" to {}[{}]", key, name, uri);
        connector.put(new Request(RequestType.PUT, key, value));
    }

    @Override
    public String get(String key) {
        LOG.info("Forwarding get request \"{}\" to {}[{}]", key, name, uri);
        return connector.get(new Request(RequestType.GET, key, null)).getValue();
    }

    @Override
    public Connector getConnector() {
        return connector;
    }

}
