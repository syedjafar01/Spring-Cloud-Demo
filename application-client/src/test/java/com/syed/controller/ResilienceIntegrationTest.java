package com.syed.controller;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "resilience4j.circuitbreaker.instances.service.wait-duration-in-open-state=1s"
})
class ResilienceIntegrationTest {

    private static MockWebServer unavailableService;

    @Autowired
    private CallingService callingService;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @BeforeAll
    static void startServer() throws IOException {
        unavailableService = new MockWebServer();
        unavailableService.start();

        for (int i = 0; i < 20; i++) {
            unavailableService.enqueue(new MockResponse().setResponseCode(503));
        }
    }

    @AfterAll
    static void stopServer() throws IOException {
        unavailableService.shutdown();
    }

    @DynamicPropertySource
    static void serviceInstance(DynamicPropertyRegistry registry) {
        registry.add("eureka.client.enabled", () -> false);
        registry.add("spring.cloud.discovery.client.simple.instances.service[0].uri",
                () -> unavailableService.url("/").toString());
    }

    @Test
    void shouldReturnFallbackAndOpenCircuitWhenServiceIsUnavailable() {
        for (int i = 0; i < 3; i++) {
            assertThat(callingService.callService())
                    .isEqualTo("Service temporarily unavailable");
        }

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("service");

        assertThat(circuitBreaker.getState())
                .isEqualTo(CircuitBreaker.State.OPEN);

        int requestsBeforeOpen = unavailableService.getRequestCount();

        assertThat(callingService.callService())
                .isEqualTo("Service temporarily unavailable");

        assertThat(unavailableService.getRequestCount())
                .isEqualTo(requestsBeforeOpen);
    }
}
