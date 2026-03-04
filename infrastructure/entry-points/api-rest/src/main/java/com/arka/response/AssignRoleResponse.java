package com.arka.response;

import com.arka.dto.output.AssignRoleOutput;
import com.arka.model.enums.RoleName;

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
