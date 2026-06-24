package com.arka.usecase;

import com.arka.dto.output.AssignRoleOutput;
import com.arka.entities.Role;
import com.arka.entities.User;
import com.arka.gateway.repository.UserGateway;
import com.arka.service.RoleService;
import com.arka.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AssignRoleUseCase {

    private final UserGateway userGateway;

    private final RoleService roleService;
    private final UserService userService;

    public AssignRoleOutput execute(Long userId, Long roleId) {

        User foundUser = userService.findById(userId);
        Role foundRole = roleService.findById(roleId);

        foundUser.assignRole(foundRole);

        userGateway.save(foundUser);

        return new AssignRoleOutput(
                foundRole.getId(),
                foundUser.getUsername(),
                foundUser.getRole().getName(),
                foundUser.getRole().getDescription());
    }
}
