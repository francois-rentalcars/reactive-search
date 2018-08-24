package org.poc.reactive.search.service;

import org.poc.reactive.search.infrastructure.provider1.Provider1Client;
import org.poc.reactive.search.infrastructure.provider2.Provider2Client;
import org.poc.reactive.search.infrastructure.provider3.Provider3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class SearchService {

    @Autowired
    private Provider1Client provider1Client;

    @Autowired
    private Provider2Client provider2Client;

    @Autowired
    private Provider3Client provider3Client;

    public Flux<String> search() {
        return Flux
                .merge(
                    provider1Client.search(),
                    provider2Client.search(),
                    provider3Client.search()
                );
    }

}
