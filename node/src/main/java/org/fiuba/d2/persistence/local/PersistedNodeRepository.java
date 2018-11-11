package org.fiuba.d2.persistence.local;

import org.fiuba.d2.persistence.local.LocalToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersistedNodeRepository extends JpaRepository<PersistedNode, Integer> {
}
