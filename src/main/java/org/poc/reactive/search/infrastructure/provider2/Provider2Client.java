package org.poc.reactive.search.infrastructure.provider2;

import org.poc.reactive.search.infrastructure.wiremock.WireMockHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Component
public class Provider2Client {

    private final WebClient webClient = WebClient.create();

    public Mono<String> search() {
        WireMockHolder.getWireMockServer()
                .stubFor(get(urlEqualTo("/provider2")).willReturn(aResponse()
                .withFixedDelay(3000)
                .withBody("RESULT FROM PROVIDER 2")));

        return webClient.get()
                .uri("http://localhost:8081/provider2")
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(String.class));

    }

}
