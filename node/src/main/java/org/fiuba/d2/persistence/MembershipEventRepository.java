package org.fiuba.d2.persistence;

import org.fiuba.d2.model.membership.MembershipEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipEventRepository extends JpaRepository<MembershipEvent, Integer> {

}
