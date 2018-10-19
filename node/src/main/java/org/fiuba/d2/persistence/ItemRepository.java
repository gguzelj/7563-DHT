package org.fiuba.d2.persistence;

import org.fiuba.d2.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, String> {

}
