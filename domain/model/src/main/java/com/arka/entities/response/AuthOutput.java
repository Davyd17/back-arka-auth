package com.arka.entities.response;

import com.arka.entities.User;

public record AuthOutput(
        String accessToken,
        String tokenType,
        Long expiresIn,
        User userResponse
) {
}
