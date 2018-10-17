package org.fiuba.d2.persistence;

public interface KeyValueRepository {

    void put(String key, String value);

    String get(String key);

}
