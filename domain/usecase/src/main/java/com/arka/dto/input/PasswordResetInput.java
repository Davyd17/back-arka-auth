package com.arka.dto.input;

public record PasswordResetInput(
        String email,
        String newPassword,
        String token
) {
}
