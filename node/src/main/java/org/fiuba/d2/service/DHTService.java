package org.fiuba.d2.service;

import org.fiuba.d2.persistence.KeyValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DHTService {

    private final KeyValueRepository keyValueRepository;

    @Autowired
    public DHTService(KeyValueRepository keyValueRepository) {
        this.keyValueRepository = keyValueRepository;
    }

    public void put(String key, String value) {
        keyValueRepository.put(key, value);
    }

    public String get(String key) {
        return keyValueRepository.get(key);
    }

}
