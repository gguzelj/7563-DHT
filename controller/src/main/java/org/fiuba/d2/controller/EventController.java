package org.fiuba.d2.controller;

import org.fiuba.d2.model.membership.MembershipEvent;
import org.fiuba.d2.service.MembershipEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class EventController {

    private final MembershipEventService membershipEventService;

    public EventController(MembershipEventService membershipEventService) {
        this.membershipEventService = membershipEventService;
    }

    @GetMapping("/events")
    private ResponseEntity<List<MembershipEvent>> getEvents(@RequestParam(required = false) Long timestamp) {
        return ok(membershipEventService.getEventsSince(timestamp));
    }

    @PostMapping("/events")
    private ResponseEntity<String> newEvent(@RequestBody MembershipEvent membershipEvent) {
        membershipEventService.addNewEvent(membershipEvent);
        return ok("OK");
    }

}
