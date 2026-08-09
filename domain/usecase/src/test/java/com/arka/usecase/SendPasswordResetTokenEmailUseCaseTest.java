package com.arka.usecase;

import com.arka.dto.input.UserRegisterInput;
import com.arka.dto.value.PasswordResetTokenEmailDto;
import com.arka.dto.value.PasswordResetTokenPolicy;
import com.arka.entities.PasswordResetToken;
import com.arka.entities.User;
import com.arka.gateway.CloudStorageGateway;
import com.arka.gateway.EmailGateway;
import com.arka.gateway.repository.PasswordResetTokenGateway;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendPasswordResetTokenEmailUseCaseTest {

    @Mock
    private UserService userService;
    @Mock
    private PasswordResetTokenGateway passwordResetTokenGateway;
    @Mock
    private PasswordResetTokenPolicy passwordResetPolicy;
    @Mock
    private EmailGateway emailGateway;
    @Mock
    private CloudStorageGateway storageGateway;

    @Mock
    private UserGateway userGateway;
    @Mock
    private PasswordEncryptionGateway passwordEncryption;
    @Mock
    private RoleService roleService;
    @Mock
    private AuthService authService;


    @InjectMocks
    private SendPasswordResetTokenEmailUseCase useCase;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    private static final String EMAIL = "john@arka.com";

    private UserRegisterInput buildInput() {
        return new UserRegisterInput(
                "johndoe",
                "john",
                "doe",
                "john@arka.com",
                "plainPassword123");
    }

    @Test
    void shouldCreateNewTokenAndSendEmailWhenTokenDoesNotExist() {
        User user = User.create("johndoe", EMAIL, "pass", null);
        Instant expiration = Instant.now().plusSeconds(3600);

        when(userService.findByEmail(EMAIL)).thenReturn(user);
        when(passwordResetPolicy.getExpiration()).thenReturn(expiration);
        when(passwordResetTokenGateway.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(storageGateway.getPasswordResetEmailTemplate()).thenReturn("<html>Template</html>");

        useCase.execute(EMAIL);

        verify(passwordResetTokenGateway).save(any(PasswordResetToken.class));
        verify(emailGateway).sendPasswordResetToken(any(PasswordResetTokenEmailDto.class));
    }

    @Test
    void shouldRegenerateTokenAndSendEmailWhenTokenAlreadyExists() {
        User user = User.create("johndoe", EMAIL, "pass", null);
        PasswordResetToken existingToken = PasswordResetToken.create(user.getId(), Instant.now());
        Instant newExpiration = Instant.now().plusSeconds(3600);

        when(userService.findByEmail(EMAIL)).thenReturn(user);
        when(passwordResetPolicy.getExpiration()).thenReturn(newExpiration);
        when(passwordResetTokenGateway.findByUserId(user.getId())).thenReturn(Optional.of(existingToken));
        when(storageGateway.getPasswordResetEmailTemplate()).thenReturn("<html>Template</html>");

        useCase.execute(EMAIL);

        verify(passwordResetTokenGateway).save(existingToken);
        verify(emailGateway).sendPasswordResetToken(any(PasswordResetTokenEmailDto.class));
    }

    @Test
    void shouldThrowWhenInputIsNull() {
        assertThatThrownBy(() -> registerUserUseCase.execute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing registration user input");

        verifyNoInteractions(userGateway, passwordEncryption, roleService, authService);
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
        verifyNoInteractions(userGateway, passwordEncryption, roleService, authService);
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
        verifyNoInteractions(userGateway, passwordEncryption, roleService, authService);
    }
}

