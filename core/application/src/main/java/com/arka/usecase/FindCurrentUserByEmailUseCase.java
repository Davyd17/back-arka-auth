package com.arka.usecase;

import com.arka.dto.output.UserOutput;
import com.arka.mapper.UserMapper;
import com.arka.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FindCurrentUserByEmailUseCase {

    private final UserService userService;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    public UserOutput execute(String email){

        return userMapper.toOutput(
                userService.findByEmail(email));
    }
}
