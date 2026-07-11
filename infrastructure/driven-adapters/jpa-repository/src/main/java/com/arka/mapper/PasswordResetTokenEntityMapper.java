package com.arka.mapper;

import com.arka.entities.PasswordResetToken;
import com.arka.tables.PasswordResetTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PasswordResetTokenEntityMapper {

    @Mapping(target = "userId", source = "user.id")
    PasswordResetToken toDomain(PasswordResetTokenEntity entity);

    @Mapping(target = "user.id", source = "userId")
    PasswordResetTokenEntity toEntity(PasswordResetToken domain);
}
