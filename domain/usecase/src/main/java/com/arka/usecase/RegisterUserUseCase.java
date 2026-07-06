package com.arka.usecase;

import com.arka.dto.input.UserRegisterInput;
import com.arka.dto.output.AuthRegisterOutput;
import com.arka.mapper.UserMapper;
import com.arka.entities.User;
import com.arka.enums.RoleName;
import com.arka.gateway.security.PasswordEncryptionGateway;
import com.arka.gateway.repository.UserGateway;
import com.arka.service.AuthService;
import com.arka.service.RoleService;
import com.arka.service.UserService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;

@RequiredArgsConstructor
public class RegisterUserUseCase {

   private final UserGateway userGateway;
   private final PasswordEncryptionGateway passwordEncryption;

   private final UserService userService;
   private final RoleService roleService;
   private final AuthService authService;

   private final UserMapper userMapper =
           Mappers.getMapper(UserMapper.class);


    public AuthRegisterOutput execute(UserRegisterInput input) {

        if(input == null){
            throw new IllegalArgumentException("Missing registration user input");
        }

        checkUniqueFields(input);

        User savedUser = userGateway.save(buildUser(input));

        return new AuthRegisterOutput(
                authService.buildAuthToken(savedUser),
                userMapper.toOutput(savedUser)
        );

    }

    private void checkUniqueFields(UserRegisterInput input){

        userService.checkExistsByEmail(input.email());
        userService.checkExistsByUsername(input.username());
    }

    private User buildUser(UserRegisterInput input) {

        return User.create(
                input.username(),
                input.email(),
                passwordEncryption.encodePassword(input.password()),
                roleService.findByName(RoleName.USER)
        );
    }







}
