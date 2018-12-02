package org.fiuba.d2.configuration;

import org.fiuba.d2.connector.Connector;
import org.fiuba.d2.model.node.Seed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@Configuration
public class SeedsConfiguration {

    @Value("${dht.ring.seeds:#{null}}")
    private String seedsUris;

    private RestTemplate restTemplate;

    public SeedsConfiguration(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Bean
    public List<Seed> seeds() {
        if (isNull(seedsUris))
            return new ArrayList<>();
        return Stream.of(seedsUris.split(",")).map(uri -> new Seed(new Connector(uri, restTemplate))).collect(toList());
    }

}
