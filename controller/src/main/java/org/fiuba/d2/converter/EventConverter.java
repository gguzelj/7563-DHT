package org.fiuba.d2.converter;

import org.fiuba.d2.dto.Event;
import org.fiuba.d2.dto.EventType;
import org.fiuba.d2.dto.Token;
import org.fiuba.d2.model.membership.MembershipEvent;
import org.fiuba.d2.model.node.Node;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class EventConverter {

    public static List<Event> convert(List<MembershipEvent> membershipEvents) {
        return membershipEvents.stream().map(EventConverter::convert).collect(toList());
    }

    private static Event convert(MembershipEvent event) {
        Node node = event.getNode();
        return new Event(event.getTimestamp(),
                EventType.valueOf(event.getType().name()),
                node.getId(),
                node.getName(),
                node.getUri(),
                TokenConverter.convert(node.getTokens()));
    }
}
