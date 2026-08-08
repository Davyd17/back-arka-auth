package com.arka.gateway;

import com.arka.dto.input.CreateContactRequest;
import com.arka.dto.output.ContactResponse;

public interface CoreServiceGateway {

    ContactResponse createContact(CreateContactRequest request);
}
