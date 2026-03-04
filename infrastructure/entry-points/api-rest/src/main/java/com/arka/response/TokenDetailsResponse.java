package com.arka.response;

import java.time.Instant;

public record TokenDetailsResponse(

        String accessToken,
        String tokenType,
        Instant expiresOn
) {
}
