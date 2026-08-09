package com.arka.usecase;

import com.arka.dto.input.CreateContactRequest;
import com.arka.dto.input.UserRegisterInput;
import com.arka.dto.output.AuthRegisterOutput;
import com.arka.dto.output.ContactResponse;
import com.arka.dto.output.UserOutput;
import com.arka.exceptions.RegistrationException;
import com.arka.gateway.CoreServiceGateway;
import com.arka.entities.User;
import com.arka.enums.RoleName;
import com.arka.gateway.security.PasswordEncryptionGateway;
import com.arka.gateway.repository.UserGateway;
import com.arka.service.AuthService;
import com.arka.service.RoleService;
import com.arka.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RegisterUserUseCase {

   private final UserGateway userGateway;
   private final CoreServiceGateway coreServiceGateway;

   private final PasswordEncryptionGateway passwordEncryption;

   private final UserService userService;
   private final RoleService roleService;
   private final AuthService authService;

    public AuthRegisterOutput execute(UserRegisterInput input) {

        if(input == null){
            throw new IllegalArgumentException("Missing registration user input");
        }

        checkUniqueFields(input);

        User savedUser = userGateway.save(buildUser(input));

        ContactResponse contact = trySaveContact(input, savedUser.getId());

        return new AuthRegisterOutput(
                authService.buildAuthToken(savedUser),
                buildUserOutput(savedUser, contact));
    }

    private ContactResponse trySaveContact(UserRegisterInput input, Long savedUserId){

        try {
            return coreServiceGateway.createContact(new CreateContactRequest(
                    input.name(), input.lastName(), input.email()));

        } catch (Exception e) {
            log.error("Failed to create contact for user {}, rolling back registration: {}",
                    savedUserId, e.getMessage());
            userGateway.deleteById(savedUserId);
            throw new RegistrationException("Failed to complete registration");
        }
    }

    private void checkUniqueFields(UserRegisterInput input){

        userService.checkExistsByEmail(input.email());
        userService.checkExistsByUsername(input.username());
    }

    private UserOutput buildUserOutput(User user, ContactResponse contact){

        return new UserOutput(
                user.getId(),
                contact.name(),
                contact.lastName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt());
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
