package com.arka.dto.output;

import com.arka.entities.Role;

import java.time.Instant;

public record UserOutput(

        Long id,
        String name,
        String lastName,
        String username,
        String email,
        Role role,
        Instant createdAt
) {
}
