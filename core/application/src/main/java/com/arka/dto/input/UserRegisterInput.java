package com.arka.dto.input;

public record UserRegisterInput(

        String username,
        String email,
        String password
) {
}
