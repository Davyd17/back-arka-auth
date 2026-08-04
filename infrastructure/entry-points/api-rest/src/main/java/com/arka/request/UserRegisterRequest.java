package com.arka.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(

        @NotBlank(message = "El username es obligartorio")
        String username,

        @NotBlank(message = "El email es obligartorio")
        @Email(message = "El email ingresado no es valido")
        String email,

        @NotBlank(message = "El password es obligartorio")
        @Size(min = 8, message = "El password requiere minimo 8 caracteres")
        String password

) {


}
