package com.arka.mapper;

import com.arka.entities.VerificationCode;
import com.arka.tables.VerificationCodeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserEntityMapper.class)
public interface VerificationCodeEntityMapper {

    VerificationCode toDomain(VerificationCodeEntity entity);

    VerificationCodeEntity toEntity(VerificationCode entity);
}
