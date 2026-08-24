package com.arka.request;

import com.arka.exception.Required;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record PasswordResetRequest(

        @Schema(
                description = "New plain text password for the account",
                example = "N3wP@ssw0rd2026",
                minLength = 8,
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Required(field = "password")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Size(max = 50, message = "Password too long")
        String newPassword,

        @Schema(
                description = "Password reset verification token sent via email",
                example = "b5b938c7-e555-49b4-b9ff-ee9974a1f7ec",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Required(field = "token")
        String token
) {
}
