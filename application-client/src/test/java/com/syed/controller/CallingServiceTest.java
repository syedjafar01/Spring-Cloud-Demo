package com.syed.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class CallingServiceTest {

    @Test
    void shouldCreateClientControllerWithInjectedRestClient() {
        RestClient restClient = mock(RestClient.class);

        CallingService service = new CallingService(restClient);

        assertThat(service).isNotNull();
    }
}
