package com.arka.entities;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class PasswordResetToken {
    private Long id;
    private String token;
    private Long userId;
    private Instant expiresAt;
    private boolean used;

    public static PasswordResetToken create(
            Long userId, Instant expiresAt) {

        return PasswordResetToken.builder()
                .token(generateToken())
                .expiresAt(expiresAt)
                .used(false)
                .userId(userId)
                .build();
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void reGenerate(Instant expiresAt) {
        this.token = generateToken();
        this.expiresAt = expiresAt;
        this.used = false;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    public void use() {
        this.used = true;
    }

}
