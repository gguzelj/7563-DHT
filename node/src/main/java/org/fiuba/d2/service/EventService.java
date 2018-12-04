package org.fiuba.d2.service;

import org.fiuba.d2.model.membership.MembershipEvent;
import org.fiuba.d2.persistence.MembershipEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Service
public class EventService {

    private MembershipEventRepository repository;

    public EventService(MembershipEventRepository repository) {
        this.repository = repository;
    }

    public List<MembershipEvent> getEventsSince(Long timestamp) {
        return nonNull(timestamp) ? repository.findByTimestampAfter(timestamp) : repository.findAll();
    }

    public void addNewEvent(MembershipEvent event) {
        Optional<MembershipEvent> byTimestamp = repository.findByTimestamp(event.getTimestamp());
        if (!byTimestamp.isPresent()) {
            repository.saveAndFlush(event);
        }
    }

    public List<MembershipEvent> findAll() {
        return repository.findAll().stream().sorted().collect(toList());
    }

    public MembershipEvent findLastEvent() {
        return repository.findTopByOrderByTimestampDesc();
    }
}
