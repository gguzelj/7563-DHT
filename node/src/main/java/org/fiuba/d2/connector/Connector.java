package org.fiuba.d2.connector;

import org.fiuba.d2.dto.Request;
import org.fiuba.d2.dto.Response;
import org.fiuba.d2.model.membership.MembershipEvent;
import org.fiuba.d2.model.node.NodeInfo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

public class Connector {

    private final String uri;
    private final RestTemplate restTemplate;

    public Connector(String uri, RestTemplate restTemplate) {
        this.uri = uri;
        this.restTemplate = restTemplate;
    }

    public Response put(Request request) {
        ResponseEntity<Response> response = restTemplate.postForEntity(uri, request, Response.class);
        return response.getStatusCode().is2xxSuccessful() ? response.getBody() : null;
    }

    public Response get(Request request) {
        ResponseEntity<Response> response = restTemplate.postForEntity(uri, request, Response.class);
        return response.getStatusCode().is2xxSuccessful() ? response.getBody() : null;
    }

    public List<MembershipEvent> getEventsSince(Long timestamp) {
        Map<String, Object> uriVariables = new HashMap<String, Object>(){{
            put("timestamp", timestamp);
        }} ;
        ResponseEntity<List<MembershipEvent>> response = restTemplate.exchange(
                uri + "/events",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MembershipEvent>>() {
                },
                uriVariables);
        return response.getStatusCode().is2xxSuccessful() ? response.getBody() : null;
    }


}
