package com.arka.request;

import com.arka.exception.Required;
import jakarta.validation.constraints.Size;

public record PasswordResetRequest(

        @Required(field = "password")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String newPassword
) {
}
