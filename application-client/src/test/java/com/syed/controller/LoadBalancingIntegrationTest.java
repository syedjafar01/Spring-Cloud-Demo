package com.syed.controller;

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
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LoadBalancingIntegrationTest {

    private static MockWebServer instance1;
    private static MockWebServer instance2;

    @Autowired
    private CallingService callingService;

    @BeforeAll
    static void startServers() throws IOException {
        instance1 = new MockWebServer();
        instance2 = new MockWebServer();
        instance1.start();
        instance2.start();

        instance1.enqueue(new MockResponse().setBody("Hello from instance-1"));
        instance1.enqueue(new MockResponse().setBody("Hello from instance-1"));
        instance2.enqueue(new MockResponse().setBody("Hello from instance-2"));
        instance2.enqueue(new MockResponse().setBody("Hello from instance-2"));
    }

    @AfterAll
    static void stopServers() throws IOException {
        instance1.shutdown();
        instance2.shutdown();
    }

    @DynamicPropertySource
    static void serviceInstances(DynamicPropertyRegistry registry) {
        registry.add("eureka.client.enabled", () -> false);
        registry.add("spring.cloud.discovery.client.simple.instances.service[0].uri",
                () -> instance1.url("/").toString());
        registry.add("spring.cloud.discovery.client.simple.instances.service[1].uri",
                () -> instance2.url("/").toString());
    }

    @Test
    void shouldLoadBalanceRequestsAcrossServiceInstances() {
        Set<String> responses = new HashSet<>();

        for (int i = 0; i < 4; i++) {
            responses.add(callingService.callService());
        }

        assertThat(responses)
                .containsExactlyInAnyOrder("Hello from instance-1", "Hello from instance-2");
    }
}
