package com.arka.dto.output;

import com.arka.model.enums.RoleName;

public record AssignRoleOutput(
        Long userId,
        String username,
        RoleName roleName,
        String roleDescription
) {
}
