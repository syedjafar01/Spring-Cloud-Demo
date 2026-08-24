package com.syed.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class CallingServiceTest {

    @Test
    void shouldReturnResponseFromDiscoveredService() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        RestClient restClient = restClientBuilder.build();
        CallingService callingService = new CallingService(restClient);

        server.expect(requestTo("http://service/"))
                .andRespond(withSuccess("Hello from test-instance", MediaType.TEXT_PLAIN));

        String response = callingService.callService();

        assertThat(response).isEqualTo("Hello from test-instance");
        server.verify();
    }
}
