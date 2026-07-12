package com.arka.dto.value;

import java.time.Instant;

/**
 * Domain policy defining the rules for verification code lifecycle.
 * Holds configuration values that govern code expiration behavior.
 */
public record VerificationCodePolicy(long codeExpirationMs) {

    /**
     * Calculates the expiration timestamp for a newly generated verification code.
     *
     * @return the absolute expiration time based on the current instant
     */
    public Instant getExpiration(){
        return Instant.now().plusMillis(codeExpirationMs);
    }
}
