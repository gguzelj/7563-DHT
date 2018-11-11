package org.fiuba.d2.controller;

import org.fiuba.d2.converter.EventConverter;
import org.fiuba.d2.dto.Event;
import org.fiuba.d2.dto.Request;
import org.fiuba.d2.dto.Response;
import org.fiuba.d2.model.membership.MembershipEvent;
import org.fiuba.d2.model.node.Node;
import org.fiuba.d2.model.node.NodeInfo;
import org.fiuba.d2.service.DHTService;
import org.fiuba.d2.service.MembershipEventService;
import org.fiuba.d2.service.NodeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.fiuba.d2.dto.RequestType.GET;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@Controller
public class RestController {

    private final DHTService dhtService;
    private final NodeInfoService nodeInfoService;
    private final MembershipEventService membershipEventService;

    @Autowired
    public RestController(DHTService dhtService,
                          NodeInfoService nodeInfoService,
                          MembershipEventService membershipEventService) {
        this.dhtService = dhtService;
        this.nodeInfoService = nodeInfoService;
        this.membershipEventService = membershipEventService;
    }

    @PostMapping
    public ResponseEntity<Response> post(@RequestBody Request request) {
        if (GET.equals(request.getType()))
            return get(request);
        return put(request);
    }

    private ResponseEntity<Response> get(Request request) {
        String value = dhtService.get(request.getKey());
        return nonNull(value) ? ok(new Response(request.getKey(), value)) : notFound().build();
    }

    private ResponseEntity<Response> put(Request request) {
        dhtService.put(request.getKey(), request.getValue());
        return ok(new Response(request.getKey(), request.getValue()));
    }

    @GetMapping("/info")
    private ResponseEntity<NodeInfo> getNodeInfo() {
        return ok(nodeInfoService.getHostInfo());
    }

    @GetMapping("/events")
    private ResponseEntity<List<Event>> getEvents(@RequestParam(required = false) Long timestamp) {
        return ok(EventConverter.convert(membershipEventService.getEventsSince(timestamp)));
    }

}