package org.poc.reactive.search.infrastructure.provider3;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import org.poc.reactive.search.infrastructure.wiremock.WireMockHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import rx.Observable;
import rx.Single;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Component
public class Provider3Client {

    private final WebClient webClient = WebClient.create();

    public Mono<String> search() {
        WireMockHolder.getWireMockServer()
                .stubFor(get(urlEqualTo("/provider3")).willReturn(aResponse()
                        .withFixedDelay(1000)
                        .withStatus(500)
                        .withBody("RESULT FROM PROVIDER 3")
                ));

        return Mono.create(consumer -> new Provider3HystrixCommand(webClient)
                .observe()
                .subscribe(consumer::success, consumer::error));

    }

    private static class Provider3HystrixCommand extends HystrixObservableCommand<String> {

        private final WebClient webClient;

        protected Provider3HystrixCommand(WebClient webClient) {
            super(Setter
                    .withGroupKey(HystrixCommandGroupKey.Factory.asKey("PROVIDER_3"))
                    .andCommandPropertiesDefaults(
                            HystrixCommandProperties.Setter()
                                    .withExecutionTimeoutEnabled(true)
                                    .withExecutionTimeoutInMilliseconds(5000)
                    ));
            this.webClient = webClient;
        }

        @Override
        protected Observable<String> construct() {
            return Single
                    .create((Single.OnSubscribe<String>) singleSubscriber -> {
                        Mono<String> resultAsMono = doSearch();
                        resultAsMono.subscribe(singleSubscriber::onSuccess, singleSubscriber::onError);
                    })
                    .toObservable();
        }

        @Override
        protected Observable<String> resumeWithFallback() {
            return Single.just("FALLBACK FOR PROVIDER 3: " + this.getFailedExecutionException().getMessage()).toObservable();
        }

        private Mono<String> doSearch() {
            return webClient.get()
                    .uri("http://localhost:8081/provider3")
                    .exchange()
                    .flatMap(clientResponse -> {
                        if (clientResponse.statusCode().is5xxServerError()) {
                            throw new IllegalStateException("Provider 3 has a problem");
                        } else {
                            return clientResponse.bodyToMono(String.class);
                        }
                    });
        }

    }

}
