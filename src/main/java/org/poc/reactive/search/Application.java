package org.poc.reactive.search;

import org.poc.reactive.search.infrastructure.wiremock.WireMockHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        WireMockHolder.startWireMockIfNotStarted();
        SpringApplication.run(Application.class, args);
    }

}
