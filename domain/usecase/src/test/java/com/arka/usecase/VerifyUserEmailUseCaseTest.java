package com.arka.usecase;

import com.arka.entities.User;
import com.arka.entities.VerificationCode;
import com.arka.exceptions.InvalidCodeException;
import com.arka.exceptions.NotFoundException;
import com.arka.gateway.repository.UserGateway;
import com.arka.gateway.repository.VerificationCodeGateway;
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
class VerifyUserEmailUseCaseTest {

    @Mock private VerificationCodeGateway verificationCodeGateway;
    @Mock private UserService userService;
    @Mock private UserGateway userGateway;

    @InjectMocks
    private VerifyUserEmailUseCase useCase;

    private static final String EMAIL = "john@arka.com";
    private static final String CODE = "123456";

    @Test
    void shouldVerifyUserEmailSuccessfully() {
        User user = User.create("johndoe", EMAIL, "pass", null);
        VerificationCode verificationCode =
                VerificationCode.create(user, Instant.now().plus(15, ChronoUnit.MINUTES));

        when(userService.findByEmail(EMAIL)).thenReturn(user);
        when(verificationCodeGateway
                .findByUserId(user.getId()))
                .thenReturn(Optional.of(verificationCode));

        useCase.execute(EMAIL, verificationCode.getCode());

        assertThat(user.isVerified()).isTrue();
        assertThat(verificationCode.isUsed()).isTrue();
        verify(verificationCodeGateway).save(verificationCode);
        verify(userGateway).save(user);
    }

    @Test
    void shouldThrowWhenVerificationCodeNotFound() {
        User user = User.create("johndoe", EMAIL, "pass", null);

        when(userService.findByEmail(EMAIL)).thenReturn(user);
        when(verificationCodeGateway.findByUserId(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(EMAIL, CODE))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Invalid verification code");

        verifyNoInteractions(userGateway);
    }

    @Test
    void shouldThrowWhenCodeIsInvalidOrExpired() {
        User user = User.create("johndoe", EMAIL, "pass", null);
        VerificationCode expiredCode = VerificationCode.create(
                user, Instant.now().minus(1, ChronoUnit.MINUTES));

        when(userService.findByEmail(EMAIL)).thenReturn(user);
        when(verificationCodeGateway.findByUserId(user.getId())).thenReturn(Optional.of(expiredCode));

        assertThatThrownBy(() -> useCase.execute(EMAIL, CODE))
                .isInstanceOf(InvalidCodeException.class)
                .hasMessage("Expired or invalid code");

        verify(userGateway, never()).save(any());
    }
}
