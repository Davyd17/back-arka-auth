package com.arka.dto.value;

import java.time.Instant;

public record TokenDetailsDto(

        String accessToken,
        String tokenType,
        Instant expiresOn
) {
}
