package org.fiuba.d2.model.node;

import org.fiuba.d2.dto.Request;
import org.fiuba.d2.dto.RequestType;
import org.fiuba.d2.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.List;

@Entity
@DiscriminatorValue("REMOTE")
public class RemoteNode extends NodeImpl {

    @Transient
    private RestTemplate restTemplate;

    private RemoteNode() {
    }

    public RemoteNode(String name, String uri, NodeStatus status, List<Token> tokens, RestTemplate restTemplate) {
        super(name, uri, status, tokens);
        this.restTemplate = restTemplate;
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
