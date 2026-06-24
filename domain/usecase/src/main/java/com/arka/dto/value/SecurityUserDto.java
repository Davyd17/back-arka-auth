package com.arka.dto.value;

import com.arka.entities.Role;

public record SecurityUserDto(

        String emailAsUsername,
        String password,
        Role role
) {
}
