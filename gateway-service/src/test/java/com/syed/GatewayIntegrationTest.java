package com.syed;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GatewayIntegrationTest {

    private static MockWebServer backend;

    @LocalServerPort
    private int gatewayPort;

    @BeforeAll
    static void startBackend() throws IOException {
        backend = new MockWebServer();
        backend.start();
    }

    @AfterAll
    static void stopBackend() throws IOException {
        backend.shutdown();
    }

    @DynamicPropertySource
    static void configureTestDiscovery(DynamicPropertyRegistry registry) {
        registry.add("eureka.client.enabled", () -> false);
        registry.add("spring.cloud.discovery.client.simple.instances.greeting-service[0].uri",
                () -> backend.url("/").toString());
        registry.add("management.tracing.sampling.probability", () -> "0");
    }

    @Test
    void shouldRouteServiceRequestToGreetingService() throws InterruptedException {
        backend.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("Hello from gateway test"));

        webTestClient()
                .get()
                .uri("/service?source=integration-test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Hello from gateway test");

        RecordedRequest request = backend.takeRequest(2, TimeUnit.SECONDS);
        assertThat(request).isNotNull();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/?source=integration-test");
    }

    @Test
    void shouldPropagateDownstreamServerError() {
        backend.enqueue(new MockResponse()
                .setResponseCode(503)
                .setBody("Greeting service unavailable"));

        webTestClient()
                .get()
                .uri("/service")
                .exchange()
                .expectStatus().isEqualTo(503)
                .expectBody(String.class)
                .isEqualTo("Greeting service unavailable");
    }

    private WebTestClient webTestClient() {
        return WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + gatewayPort)
                .build();
    }
}
