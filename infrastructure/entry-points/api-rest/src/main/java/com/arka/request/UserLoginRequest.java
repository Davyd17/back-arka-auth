package com.arka.request;


import com.arka.exception.Required;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {

    @Schema(
            description = "User email address",
            example = "user@arkadistributions.co",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Required(field = "email")
    private String email;

    @Schema(
            description = "Account password",
            example = "P@ssw0rd2026",
            minLength = 8,
            maxLength = 50,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Required(field = "password")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Size(max = 50, message = "Password too long")
    private String password;

}
