package com.arka.response;

import java.time.Instant;

public record UserResponse(

        Long id,
        String username,
        String email,
        RoleResponse role,
        boolean enabled,
        Instant createdAt
) {

}
