package org.poc.reactive.search.infrastructure.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class WireMockHolder {

    private static WireMockServer wireMockServer;

    public static synchronized WireMockServer startWireMockIfNotStarted() {
        if (wireMockServer == null) {
            wireMockServer = new WireMockServer(new WireMockConfiguration().port(8081));
            wireMockServer.start();
        }
        return wireMockServer;
    }

    public static WireMockServer getWireMockServer() {
        return wireMockServer;
    }

}
