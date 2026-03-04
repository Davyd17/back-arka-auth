package com.arka.response;

public record UserSummaryResponse(
        Long id,
        String username,
        String email
) {
}
