package com.arka.gateway.repository;

import com.arka.model.Role;
import com.arka.model.enums.RoleName;

import java.util.Optional;

public interface RoleRepositoryGateway {

    Optional<Role> findById(Long id);

    Optional<Role> findByName(RoleName name);
}
