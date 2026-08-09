package com.arka.dto.output;

public record ContactResponse(
        Long id,
        String name,
        String lastName,
        String email
) {
}
