package com.arka.mapper;

import com.arka.model.Role;
import com.arka.tables.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleEntityMapper {

    Role toDomain(RoleEntity roleEntity);

    RoleEntity toEntity(Role role);
}
