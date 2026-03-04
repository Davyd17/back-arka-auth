package com.arka.mapper;

import com.arka.dto.output.AuthLoginOutput;
import com.arka.dto.output.AuthRegisterOutput;
import com.arka.dto.value.TokenDetailsDto;
import com.arka.response.AuthLoginResponse;
import com.arka.response.AuthRegisterResponse;
import com.arka.response.TokenDetailsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserRestMapper.class)
public interface AuthRestMapper {

    TokenDetailsResponse toResponse(TokenDetailsDto dto);

    AuthRegisterResponse toResponse(AuthRegisterOutput dto);

    AuthLoginResponse toResponse(AuthLoginOutput output);
}
