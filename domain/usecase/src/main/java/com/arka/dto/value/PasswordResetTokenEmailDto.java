package com.arka.dto.value;

public record PasswordResetTokenEmailDto(
        String recipient,
        String username,
        String templateBody,
        String token
) {
}
