package org.fiuba.d2.model;

import org.fiuba.d2.dto.Request;
import org.fiuba.d2.dto.RequestType;
import org.fiuba.d2.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.util.List;

public class RemoteNode implements Node {

    private NodeStatus status = NodeStatus.AVAILABLE;
    private RestTemplate restTemplate;

    public RemoteNode(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public NodeStatus getStatus() {
        return status;
    }

    @Override
    public List<Token> getTokens() {
        return null;
    }

    @Override
    public void put(String key, String value) {
        Request request = new Request(RequestType.PUT, key, value);
        String uri = "localhost:80801";
        ResponseEntity<Response> response = restTemplate.postForEntity(uri, request, Response.class);
    }

    @Override
    public String get(String key) {
        Request request = new Request(RequestType.GET, key, null);
        String uri = "localhost:80801";
        ResponseEntity<Response> response = restTemplate.postForEntity(uri, request, Response.class);
        return response.getBody().getValue();
    }
}
