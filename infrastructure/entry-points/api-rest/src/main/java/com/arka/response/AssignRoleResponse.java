package com.arka.response;

import com.arka.dto.output.AssignRoleOutput;
import com.arka.enums.RoleName;

public record AssignRoleResponse(

        Long userId,
        String username,
        RoleName roleName,
        String roleDescription
) {

    public static AssignRoleResponse of(AssignRoleOutput output) {
        return new AssignRoleResponse(
                output.userId(),
                output.username(),
                output.roleName(),
                output.roleDescription()
        );
    }
}
