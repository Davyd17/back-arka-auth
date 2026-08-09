package com.arka.usecase;

import com.arka.dto.input.CreateContactRequest;
import com.arka.dto.input.UserRegisterInput;
import com.arka.dto.output.AuthRegisterOutput;
import com.arka.dto.output.ContactResponse;
import com.arka.dto.value.TokenDetailsDto;
import com.arka.entities.Role;
import com.arka.entities.User;
import com.arka.enums.RoleName;
import com.arka.gateway.CoreServiceGateway;
import com.arka.gateway.repository.UserGateway;
import com.arka.gateway.security.PasswordEncryptionGateway;
import com.arka.service.AuthService;
import com.arka.service.RoleService;
import com.arka.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserGateway userGateway;
    @Mock
    private CoreServiceGateway coreServiceGateway;
    @Mock
    private PasswordEncryptionGateway passwordEncryption;
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private AuthService authService;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    private UserRegisterInput buildInput() {
        return new UserRegisterInput(
                "johndoe",
                "john", "doe",
                "john@arka.com",
                "plainPassword123");
    }

    @Test
    void shouldRegisterUserSuccessfully() {

        UserRegisterInput input = buildInput();

        Role userRole = Role.create(RoleName.USER, "User role");

        User savedUser = User.create(
                input.username(), input.email(), "encodedPassword", userRole);

        ContactResponse contactResponse = new ContactResponse(
                1L, "john", "doe", "john@arka.com");

        when(roleService.findByName(RoleName.USER)).thenReturn(userRole);
        when(passwordEncryption.encodePassword(input.password())).thenReturn("encodedPassword");
        when(userGateway.save(any(User.class))).thenReturn(savedUser);
        when(coreServiceGateway.createContact(any(CreateContactRequest.class))).thenReturn(contactResponse);
        when(authService.buildAuthToken(savedUser)).thenReturn(
                new TokenDetailsDto("mock-token", "JWT", Instant.now()));

        AuthRegisterOutput output = registerUserUseCase.execute(input);

        assertThat(output).isNotNull();
        assertThat(output.tokenDetails().accessToken()).isEqualTo("mock-token");
        assertThat(output.user().username()).isEqualTo("johndoe");
        assertThat(output.user().name()).isEqualTo("john");

        verify(userService).checkExistsByEmail(input.email());
        verify(userService).checkExistsByUsername(input.username());
        verify(userGateway).save(any(User.class));
        verify(coreServiceGateway).createContact(
                new CreateContactRequest("john", "doe", "john@arka.com"));
    }

    @Test
    void shouldThrowWhenInputIsNull() {
        assertThatThrownBy(() -> registerUserUseCase.execute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing registration user input");

        verifyNoInteractions(userGateway, coreServiceGateway, passwordEncryption, roleService, authService);
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        UserRegisterInput input = buildInput();
        doThrow(new IllegalArgumentException("Email already exists"))
                .when(userService).checkExistsByEmail(input.email());

        assertThatThrownBy(() -> registerUserUseCase.execute(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already exists");

        verify(userService, never()).checkExistsByUsername(anyString());
        verifyNoInteractions(userGateway, coreServiceGateway, passwordEncryption, roleService, authService);
    }

    @Test
    void shouldThrowWhenUsernameAlreadyExists() {
        UserRegisterInput input = buildInput();
        doThrow(new IllegalArgumentException("Username already exists"))
                .when(userService).checkExistsByUsername(input.username());

        assertThatThrownBy(() -> registerUserUseCase.execute(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username already exists");

        verify(userService).checkExistsByEmail(input.email());
        verifyNoInteractions(userGateway, coreServiceGateway, passwordEncryption, roleService, authService);
    }
}
