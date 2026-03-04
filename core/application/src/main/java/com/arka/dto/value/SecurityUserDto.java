package com.arka.dto.value;

import com.arka.model.Role;

public record SecurityUserDto(

        String emailAsUsername,
        String password,
        Role role
) {
}
