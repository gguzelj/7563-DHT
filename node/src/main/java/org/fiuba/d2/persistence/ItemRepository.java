package org.fiuba.d2.persistence;

import org.fiuba.d2.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, String> {

    List<Item> findItemsByIdBetween(String from, String to);

}
