package com.arka.dto.input;

public record PasswordResetInput(
        String newPassword,
        String token
) {
}
