package com.arka;

import com.arka.dto.input.CreateContactRequest;
import com.arka.dto.output.ContactResponse;
import com.arka.gateway.CoreServiceGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoreServiceAdapter implements CoreServiceGateway {

    private final RestClient restClient;

    @Override
    public ContactResponse createContact(CreateContactRequest request) {

        return restClient.post()
                .uri("api/v1/internal/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, resp) -> {
                    throw new IllegalArgumentException("Invalid request calling create contact service");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, resp) -> {
                    throw new IllegalStateException("Unexpected error calling core service");
                })
                .body(ContactResponse.class);
    }
}
