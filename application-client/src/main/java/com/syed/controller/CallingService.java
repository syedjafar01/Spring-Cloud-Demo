package com.syed.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class CallingService {

    private final RestClient restClient;

    public CallingService(RestClient restClient) {
        this.restClient = restClient;
    }

    @GetMapping("/")
    public String callService() {
        return restClient.get()
                .uri("http://service/")
                .retrieve()
                .body(String.class);
    }
}
