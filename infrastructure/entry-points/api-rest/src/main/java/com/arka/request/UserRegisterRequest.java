package com.arka.request;

import com.arka.exception.Required;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(

        @Schema(
                description = "Unique alphanumeric username (letters, numbers, underscores)",
                example = "johnconnor_99",
                minLength = 3,
                maxLength = 30,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Required(field = "username")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$",
                message = "Username must only contain letters, numbers and underscores")
        @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
        String username,

        @Schema(
                description = "First name and middle name if applicable",
                example = "John Carlos",
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Required(field = "name")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ ]+$",
                message = "Name must only contain letters")
        @Size(max = 50, message = "Name must not exceed 50 characters")
        String name,

        @Schema(
                description = "Last name (single word, no spaces)",
                example = "Conor",
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Required(field = "lastName")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ]+$",
                message = "Last name must only contain letters without spaces")
        @Size(max = 50, message = "Last name must not exceed 50 characters")
        String lastName,

        @Schema(
                description = "Valid and unique email address",
                example = "john.conor@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Required(field = "email")
        @Email(message = "Please enter a valid email")
        String email,

        @Schema(
                description = "Plain text password",
                example = "P@ssw0rd2026",
                minLength = 8,
                maxLength = 72,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Required(field = "password")
        @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
        String password

) {


}
