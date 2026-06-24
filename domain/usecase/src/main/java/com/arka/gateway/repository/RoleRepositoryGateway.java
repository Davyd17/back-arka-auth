package com.arka.gateway.repository;

import com.arka.entities.Role;
import com.arka.enums.RoleName;

import java.util.Optional;

public interface RoleRepositoryGateway {

    Optional<Role> findById(Long id);

    Optional<Role> findByName(RoleName name);
}
