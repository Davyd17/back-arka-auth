package com.arka.usecase;

import com.arka.dto.value.SecurityUserDto;
import com.arka.mapper.UserMapper;
import com.arka.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;

@Slf4j
@RequiredArgsConstructor
public class FindSecurityUserByEmailUseCase {

    private final UserService userService;
    private final UserMapper mapper =
            Mappers.getMapper(UserMapper.class);

    public SecurityUserDto execute(String email) {

        SecurityUserDto securityUserDto = mapper.toDto(userService.findByEmail(email));

        log.debug("Security User Role: {}", securityUserDto.role());

        return securityUserDto;
    }
}

