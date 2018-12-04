package org.fiuba.d2.connector;

import org.fiuba.d2.dto.Request;
import org.fiuba.d2.dto.Response;
import org.fiuba.d2.model.exception.UnreachableNodeException;
import org.fiuba.d2.model.membership.MembershipEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class Connector {

    private static final Logger LOG = LoggerFactory.getLogger(Connector.class);

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
        try {
            ResponseEntity<Response> response = restTemplate.postForEntity(uri, request, Response.class);
            return response.getStatusCode().is2xxSuccessful() ? response.getBody() : null;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(NOT_FOUND)) {
                return new Response(request.getKey(), null);
            } else {
                LOG.error("Unexpected error while performing GET request", e);
                throw e;
            }
        }
    }

    public List<MembershipEvent> getAllEvents() {
        return getEventsSince(0L);
    }

    public List<MembershipEvent> getEventsSince(Long timestamp) {
        try {
            return restTemplate.exchange(
                    uri + "/events?timestamp=" + timestamp.toString(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<MembershipEvent>>() {
                    }).getBody();
        } catch (RestClientException e) {
            LOG.error("Error while connecting with {}", uri);
            throw new UnreachableNodeException();
        }
    }

    public void sendEvent(MembershipEvent event) {
        restTemplate.postForEntity(uri + "/events", event, String.class);
    }
}
