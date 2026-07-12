package com.arka.service;

import com.arka.entities.Role;
import com.arka.exceptions.NotFountException;
import com.arka.gateway.repository.RoleRepositoryGateway;
import com.arka.enums.RoleName;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleService {

    private final RoleRepositoryGateway roleRepositoryGateway;

    public Role findById(Long roleId) {

        return roleRepositoryGateway.findById(roleId)
                .orElseThrow(() -> new NotFountException(
                        String.format("Role with id %d not found", roleId)
                ));
    }

    public Role findByName(RoleName name) {

        return roleRepositoryGateway.findByName(name)
                .orElseThrow(() -> new NotFountException(
                        String.format("Role %s not found", name)
                ));
    }
}
