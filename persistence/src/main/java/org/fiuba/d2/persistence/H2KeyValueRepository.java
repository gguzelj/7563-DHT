package org.fiuba.d2.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static java.util.Objects.nonNull;
import static org.fiuba.d2.utils.HashGenerator.sha1;
import static org.fiuba.d2.utils.StringUtils.getHexString;

@Component
public class H2KeyValueRepository implements KeyValueRepository {

    private final static Logger LOG = LoggerFactory.getLogger(H2KeyValueRepository.class);

    private final ItemRepository itemRepository;

    @Autowired
    public H2KeyValueRepository(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void put(String key, String value) {
        LOG.info("saving key {}", key);
        itemRepository.save(convert(key, value));
    }

    @Override
    public String get(String key) {
        Item one = itemRepository.findOne(getHexString(sha1(key)));
        return nonNull(one) ? one.getValue() : null;
    }

    private Item convert(String key, String value) {
        return new Item(getHexString(sha1(key)), key, value);
    }
}
