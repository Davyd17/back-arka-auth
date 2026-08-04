package com.arka.usecase;

import com.arka.dto.input.PasswordResetInput;
import com.arka.entities.PasswordResetToken;
import com.arka.entities.User;
import com.arka.exceptions.InvalidTokenException;
import com.arka.gateway.repository.PasswordResetTokenGateway;
import com.arka.gateway.repository.UserGateway;
import com.arka.gateway.security.PasswordEncryptionGateway;
import com.arka.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetUseCaseTest {

    @Mock private PasswordResetTokenGateway passwordResetTokenGateway;
    @Mock private UserService userService;
    @Mock private UserGateway userGateway;
    @Mock private PasswordEncryptionGateway passwordEncryptionGateway;

    @InjectMocks
    private PasswordResetUseCase passwordResetUseCase;

    private PasswordResetToken buildActiveToken() {
        return PasswordResetToken.create(1L, Instant.now().plus(10, ChronoUnit.MINUTES));
    }

    @Test
    void shouldResetPasswordSuccessfully() {
        PasswordResetToken token = buildActiveToken();
        User user = User.create("johndoe", "john@arka.com", "oldPassword", null);

        when(passwordResetTokenGateway.findByToken(token.getToken()))
                .thenReturn(Optional.of(token));
        when(userService.findById(1L)).thenReturn(user);
        when(passwordEncryptionGateway.encodePassword("newPassword123"))
                .thenReturn("encodedPassword");

        passwordResetUseCase.execute(
                new PasswordResetInput("newPassword123", token.getToken()));

        assertThat(user.getPassword()).isEqualTo("encodedPassword");
        verify(userGateway).save(user);
        verify(passwordResetTokenGateway).save(token);
        assertThat(token.isUsed()).isTrue();
    }

    @Test
    void shouldThrowWhenTokenNotFound() {
        when(passwordResetTokenGateway.findByToken("invalid-token"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> passwordResetUseCase.execute(
                new PasswordResetInput("newPassword123", "invalid-token")))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Expired or invalid code");

        verifyNoInteractions(userGateway);
    }

    @Test
    void shouldThrowWhenTokenIsExpired() {
        PasswordResetToken expiredToken = PasswordResetToken.create(
                1L, Instant.now().minus(1, ChronoUnit.MINUTES));

        when(passwordResetTokenGateway.findByToken(expiredToken.getToken()))
                .thenReturn(Optional.of(expiredToken));

        assertThatThrownBy(() -> passwordResetUseCase.execute(
                new PasswordResetInput("newPassword123", expiredToken.getToken())))
                .isInstanceOf(InvalidTokenException.class);

        verifyNoInteractions(userGateway);
    }

    @Test
    void shouldThrowWhenTokenIsAlreadyUsed() {
        PasswordResetToken usedToken = buildActiveToken();
        usedToken.use();

        when(passwordResetTokenGateway.findByToken(usedToken.getToken()))
                .thenReturn(Optional.of(usedToken));

        assertThatThrownBy(() -> passwordResetUseCase.execute(
                new PasswordResetInput("newPassword123", usedToken.getToken())))
                .isInstanceOf(InvalidTokenException.class);

        verifyNoInteractions(userGateway);
    }
}
