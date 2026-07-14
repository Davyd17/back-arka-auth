package com.arka.request;

import jakarta.validation.constraints.Email;

public record ForgotPasswordRequest(
        @Email(message = "Enter a valid email address")
        String email
) {
}
