package com.arka.request;

import com.arka.exception.Required;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(

        @Required(field = "username")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$",
                message = "Username must only contain letters, numbers and underscores")
        @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
        String username,

        @Required(field = "name")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ ]+$",
                message = "Name must only contain letters")
        @Size(max = 50, message = "Name must not exceed 50 characters")
        String name,

        @Required(field = "lastName")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ]+$",
                message = "Last name must only contain letters without spaces")
        @Size(max = 50, message = "Last name must not exceed 50 characters")
        String lastName,

        @Required(field = "email")
        @Email(message = "Please enter a valid email")
        String email,

        @Required(field = "password")
        @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
        String password

) {


}
