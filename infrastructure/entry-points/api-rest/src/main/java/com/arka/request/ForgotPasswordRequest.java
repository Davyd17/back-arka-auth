package com.arka.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

public record ForgotPasswordRequest(

        @Schema(
                description = "User email address",
                example = "user@arkadistributions.co",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Email(message = "Enter a valid email address")
        String email
) {
}
