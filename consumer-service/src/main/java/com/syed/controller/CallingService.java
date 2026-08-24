package com.syed.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class CallingService {

    private static final String SERVICE_NAME = "service";
    private static final String FALLBACK_RESPONSE = "Service temporarily unavailable";

    private final RestClient restClient;

    public CallingService(RestClient restClient) {
        this.restClient = restClient;
    }

    @GetMapping("/")
    @Retry(name = SERVICE_NAME)
    @CircuitBreaker(name = SERVICE_NAME, fallbackMethod = "serviceFallback")
    public String callService() {
        return restClient.get()
                .uri("http://service/")
                .retrieve()
                .body(String.class);
    }

    private String serviceFallback(Throwable throwable) {
        return FALLBACK_RESPONSE;
    }
}
