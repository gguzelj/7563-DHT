package org.fiuba.d2.service;

import org.fiuba.d2.model.membership.MembershipEvent;
import org.fiuba.d2.persistence.MembershipEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.nonNull;

@Service
public class MembershipEventService {

    private MembershipEventRepository repository;

    public MembershipEventService(MembershipEventRepository repository) {
        this.repository = repository;
    }

    public List<MembershipEvent> getEventsSince(Long timestamp) {
        return nonNull(timestamp) ? repository.findByTimestampAfter(timestamp) : repository.findAll();
    }
}
