package com.arka.model.response;

import com.arka.model.User;

public record AuthOutput(
        String accessToken,
        String tokenType,
        Long expiresIn,
        User userResponse
) {
}
