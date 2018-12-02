package org.fiuba.d2.controller;

import org.fiuba.d2.model.membership.MembershipEvent;
import org.fiuba.d2.service.EventService;
import org.fiuba.d2.service.RingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@RestController
public class EventController {

    private static final Logger LOG = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;
    private final RingService ringService;

    public EventController(EventService eventService, RingService ringService) {
        this.eventService = eventService;
        this.ringService = ringService;
    }

    @GetMapping("/events")
    private ResponseEntity<List<MembershipEvent>> getEvents(@RequestParam(required = false) Long timestamp) {
        return ok(eventService.getEventsSince(timestamp).stream().sorted().collect(toList()));
    }

    @PostMapping("/events")
    private ResponseEntity<String> newEvent(@RequestBody MembershipEvent membershipEvent) {
        LOG.info("New event received: {}", membershipEvent);
        eventService.addNewEvent(membershipEvent);
        ringService.updateRing(membershipEvent);
        return ok("OK");
    }

}
