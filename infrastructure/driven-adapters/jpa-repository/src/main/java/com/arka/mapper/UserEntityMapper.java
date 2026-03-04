package com.arka.mapper;

import com.arka.model.User;
import com.arka.tables.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    UserEntity toEntity(User user);

    User toDomain(UserEntity userEntity);
}
