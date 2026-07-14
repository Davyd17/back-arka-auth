package com.arka.request;


import com.arka.exception.Required;
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

    @Required(field = "email")
    private String email;

    @Required(field = "password")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Size(max = 50, message = "Password too long")
    private String password;

}
