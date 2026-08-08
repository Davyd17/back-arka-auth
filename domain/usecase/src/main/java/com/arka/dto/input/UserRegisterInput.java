package com.arka.dto.input;

public record UserRegisterInput(

        String username,
        String name,
        String lastName,
        String email,
        String password
) {
}
