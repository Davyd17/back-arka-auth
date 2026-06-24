package com.arka.usecase;

import com.arka.dto.value.SecurityUserDto;
import com.arka.mapper.UserMapper;
import com.arka.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FindSecurityUserByEmailUseCase {

    private final UserService userService;
    private final UserMapper mapper = UserMapper.INSTANCE;

    public SecurityUserDto execute(String email) {

        return mapper.toDto(userService.findByEmail(email));
    }
}

