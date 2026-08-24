package com.syed.controller;

import com.netflix.discovery.EurekaClient;
import com.netflix.appinfo.InstanceInfo;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

class CallingServiceTest {

    @Test
    void shouldCreateClientController() {
        CallingService service = new CallingService();
        assertThat(service).isNotNull();
    }
}
