package com.arka.usecase;

import com.arka.dto.input.UserLoginInput;
import com.arka.dto.output.AuthLoginOutput;
import com.arka.mapper.UserMapper;
import com.arka.entities.User;
import com.arka.exceptions.InvalidCredentialsException;
import com.arka.gateway.repository.UserGateway;
import com.arka.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;

@RequiredArgsConstructor
public class LoginUserUseCase {

    private final UserGateway userGateway;

    private final AuthService authService;

    private final UserMapper userMapper =
            Mappers.getMapper(UserMapper.class);


    public AuthLoginOutput execute(UserLoginInput input) {

        if(input == null)
            throw new IllegalArgumentException("Missing login user input");

        User user = authenticateUser(input);

        return new AuthLoginOutput(
                userMapper.toSummary(user),
                authService.buildAuthToken(user)
        );
    }

    private User authenticateUser(UserLoginInput input) {

        return userGateway.findUserByEmail(input.email())
                .filter(user -> authService.
                        passwordsMatches(user.getPassword(), input.password()))
                .filter(User::isEnabled)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
    }

}
