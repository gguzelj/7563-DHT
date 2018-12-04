package org.fiuba.d2.persistence;

import org.fiuba.d2.model.membership.MembershipEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipEventRepository extends JpaRepository<MembershipEvent, Integer> {

    Optional<MembershipEvent> findByTimestamp(Long timestamp);

    List<MembershipEvent> findByTimestampAfter(Long timestamp);

    MembershipEvent findTopByOrderByTimestampDesc();

}
