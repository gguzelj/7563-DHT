package org.fiuba.d2.controller;

import org.fiuba.d2.dto.Request;
import org.fiuba.d2.dto.Response;
import org.fiuba.d2.service.DHTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.nonNull;
import static org.fiuba.d2.dto.RequestType.GET;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@Controller
public class RestController {

    private final DHTService dhtService;

    @Autowired
    public RestController(DHTService dhtService) {
        this.dhtService = dhtService;
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

}