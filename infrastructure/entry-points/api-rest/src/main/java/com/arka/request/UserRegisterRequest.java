package com.arka.request;

import com.arka.exception.Required;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(

        @Required(field = "username")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$",
                message = "Username must only contain letters, numbers and underscores")
        String username,

        @Required(field = "name")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ ]+$",
                message = "Name must only contain letters")
        String name,

        @Required(field = "lastName")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ]+$",
                message = "Last name must only contain letters without spaces")
        String lastName,

        @Required(field = "email")
        @Email(message = "Please enter a valid email")
        String email,

        @Required(field = "password")
        @Size(min = 8, message = "Password required at least 8 characters")
        String password

) {


}
