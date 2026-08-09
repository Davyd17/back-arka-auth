package com.arka.response;

import java.time.Instant;

public record UserResponse(

        Long id,
        String name,
        String lastName,
        String username,
        String email,
        RoleResponse role,
        Instant createdAt
) {

}
