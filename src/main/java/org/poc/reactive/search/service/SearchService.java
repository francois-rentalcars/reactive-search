package org.poc.reactive.search.service;

import org.poc.reactive.search.infrastructure.provider1.Provider1Client;
import org.poc.reactive.search.infrastructure.provider2.Provider2Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class SearchService {

    @Autowired
    private Provider1Client provider1Client;

    @Autowired
    private Provider2Client provider2Client;

    public Flux<String> search() {
        return Flux
                .merge(
                    provider1Client.search(),
                    provider2Client.search(),
                    Mono.delay(Duration.ofSeconds(10)).map(v -> "stop")
                );
    }

}
