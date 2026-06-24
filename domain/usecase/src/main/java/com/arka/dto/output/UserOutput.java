package com.arka.dto.output;

import com.arka.entities.Role;

import java.time.Instant;

public record UserOutput(

        Long id,
        String username,
        String email,
        Role role,
        boolean enabled,
        Instant createdAt
) {
}
