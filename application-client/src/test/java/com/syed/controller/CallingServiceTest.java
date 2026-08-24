package com.syed.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;

class CallingServiceTest {

    @Test
    void shouldReturnResponseFromDiscoveredService() {
        RestClient restClient = mock(RestClient.class, RETURNS_DEEP_STUBS);
        when(restClient.get()
                .uri("http://service/")
                .retrieve()
                .body(String.class))
                .thenReturn("Hello from instance-1");

        CallingService service = new CallingService(restClient);

        assertThat(service.callService()).isEqualTo("Hello from instance-1");
    }
}
