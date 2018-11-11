package org.fiuba.d2.connector;

import org.fiuba.d2.dto.Request;
import org.fiuba.d2.dto.Response;
import org.fiuba.d2.model.node.NodeInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.OK;

public class Connector {

    private final String uri;
    private final RestTemplate restTemplate;

    public Connector(String uri, RestTemplate restTemplate) {
        this.uri = uri;
        this.restTemplate = restTemplate;
    }

    public NodeInfo getInfo() {
        ResponseEntity<NodeInfo> response = restTemplate.getForEntity(uri, NodeInfo.class);
        return response.getStatusCode().equals(OK) ? response.getBody() : null;
    }

    public Response put(Request request) {
        return null;
    }
}
