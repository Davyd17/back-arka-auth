package com.arka.dto.value;

import java.time.Instant;

/**
 * Domain policy defining the rules for reset password token lifecycle.
 * Holds configuration values that govern token expiration behavior.
 */
public record PasswordResetTokenPolicy(long tokenExpirationMs) {

    /**
     * Calculates the expiration timestamp for a newly generated verification token.
     *
     * @return the absolute expiration time based on the current instant
     */
    public Instant getExpiration(){
        return Instant.now().plusMillis(tokenExpirationMs);
    }
}
