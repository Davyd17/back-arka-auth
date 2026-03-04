package com.arka.service;

import com.arka.gateway.repository.RoleRepositoryGateway;
import com.arka.mapper.RoleEntityMapper;
import com.arka.model.Role;
import com.arka.model.enums.RoleName;
import com.arka.repository.RoleJpaRepository;
import com.arka.tables.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoleServiceAdapterGateway implements RoleRepositoryGateway {

    private final RoleJpaRepository repository;
    private final RoleEntityMapper mapper;

    @Override
    public Optional<Role> findById(Long id) {

        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Role> findByName(RoleName name) {

        return repository.findByName(name)
                .map(mapper::toDomain);
    }
}
