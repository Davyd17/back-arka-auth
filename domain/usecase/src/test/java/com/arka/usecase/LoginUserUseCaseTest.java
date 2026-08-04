package com.arka.usecase;

import com.arka.dto.input.UserLoginInput;
import com.arka.dto.output.AuthLoginOutput;
import com.arka.dto.value.TokenDetailsDto;
import com.arka.entities.User;
import com.arka.exceptions.InvalidCredentialsException;
import com.arka.gateway.repository.UserGateway;
import com.arka.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private AuthService authService;

    @InjectMocks
    private LoginUserUseCase loginUserUseCase;

    private User buildUser() {
        return User.create("johndoe", "john@arka.com", "hashedPassword", null);
    }

    @Test
    void
    shouldReturnAuthOutputWhenCredentialsAreValid() {
        User user = buildUser();
        user.verify();

        when(userGateway.findUserByEmail("john@arka.com")).thenReturn(Optional.of(user));
        when(authService.passwordsMatches("hashedPassword", "password123")).thenReturn(true);
        when(authService.buildAuthToken(user))
                .thenReturn(new TokenDetailsDto("mock-token", "JWT", Instant.now()));

        AuthLoginOutput output = loginUserUseCase.execute(
                new UserLoginInput("john@arka.com", "password123"));

        assertThat(output.tokenDetails().accessToken()).isEqualTo("mock-token");
        assertThat(output.user().username()).isEqualTo("johndoe");
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userGateway.findUserByEmail("unknown@arka.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginUserUseCase.execute(
                new UserLoginInput("unknown@arka.com", "password123")))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Invalid email or password");
    }

    @Test
    void shouldThrowWhenPasswordDoesNotMatch() {
        User user = buildUser();

        when(userGateway.findUserByEmail("john@arka.com")).thenReturn(Optional.of(user));
        when(authService.passwordsMatches(
                "hashedPassword", "wrongPassword")).thenReturn(false);

        assertThatThrownBy(() -> loginUserUseCase.execute(
                new UserLoginInput("john@arka.com", "wrongPassword")))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void shouldThrowWhenUserIsDisabled() {
        User user = buildUser();
        user.disable();

        when(userGateway.findUserByEmail("john@arka.com")).thenReturn(Optional.of(user));
        when(authService.passwordsMatches(
                "hashedPassword", "password123")).thenReturn(true);

        assertThatThrownBy(() -> loginUserUseCase.execute(
                new UserLoginInput("john@arka.com", "password123")))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void shouldThrowWhenInputIsNull() {
        assertThatThrownBy(() -> loginUserUseCase.execute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing login user input");

        verifyNoInteractions(userGateway);
        verifyNoInteractions(authService);
    }
}
