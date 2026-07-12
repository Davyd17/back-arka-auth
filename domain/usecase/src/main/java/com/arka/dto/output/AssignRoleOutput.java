package com.arka.dto.output;

import com.arka.enums.RoleName;

public record AssignRoleOutput(
        Long userId,
        String username,
        RoleName roleName,
        String roleDescription
) {
}
