package com.arka.dto.input;

public record CreateContactRequest(
        String name,
        String lastName,
        String email
) {
}
