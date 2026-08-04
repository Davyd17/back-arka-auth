package com.arka.entities;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder(toBuilder = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@ToString
public class VerificationCode {

    private Long id;
    private String code;
    private Instant expiresAt;
    private boolean used;
    private User user;

    public static VerificationCode create(
            User user, Instant expiresAt) {

        return VerificationCode.builder()
                .code(generateCode())
                .expiresAt(expiresAt)
                .used(false)
                .user(user)
                .build();
    }

    public void reGenerate(Instant expiresAt) {
        this.code = generateCode();
        this.expiresAt = expiresAt;
    }

    private static String generateCode() {
        return UUID.randomUUID()
                .toString().substring(0, 6).toUpperCase();
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    public void use() {
        this.used = true;
    }
}
