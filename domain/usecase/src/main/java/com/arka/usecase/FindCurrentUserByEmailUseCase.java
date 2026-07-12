package com.arka.usecase;

import com.arka.dto.output.UserOutput;
import com.arka.mapper.UserMapper;
import com.arka.service.UserService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;

@RequiredArgsConstructor
public class FindCurrentUserByEmailUseCase {

    private final UserService userService;
    private final UserMapper userMapper =
            Mappers.getMapper(UserMapper.class);

    public UserOutput execute(String email){

        return userMapper.toOutput(
                userService.findByEmail(email));
    }
}
