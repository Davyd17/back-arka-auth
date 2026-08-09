package com.arka;


import com.arka.dto.input.CreateContactRequest;
import com.arka.dto.output.ContactResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(CoreServiceAdapter.class)
@Import(RestClientConfig.class)
@ActiveProfiles("test")
class CoreServiceAdapterTest {

    @Autowired
    private CoreServiceAdapter coreServiceAdapter;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${core-service.base-url}")
    private String baseUrl;


    @Test
    void shouldCreateContactSuccessfully() throws Exception{

        // given
        CreateContactRequest request = new CreateContactRequest(
                "John", "Doe", "john.doe@arka.com");

        String jsonResponse = objectMapper.writeValueAsString(
                new ContactResponse(1L, "jhon", "Doe", "john.doe@arka.com"));

        mockServer.expect(requestTo(baseUrl + "/api/v1/internal/contacts"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/json"))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john.doe@arka.com"))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // when
        ContactResponse response = coreServiceAdapter.createContact(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("john.doe@arka.com");
        mockServer.verify();
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenServerReturns5xx() {
        // given
        CreateContactRequest request = new CreateContactRequest(
                "John", "Doe", "john.doe@arka.com");

        mockServer.expect(requestTo(baseUrl + "/api/v1/internal/contacts"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServerError());

        // when & then
        assertThatThrownBy(() -> coreServiceAdapter.createContact(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Unexpected error calling core service");

        mockServer.verify();
    }
}
