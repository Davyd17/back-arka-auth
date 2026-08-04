package com.arka.usecase;

import com.arka.dto.value.VerificationCodeEmailDto;
import com.arka.dto.value.VerificationCodePolicy;
import com.arka.entities.User;
import com.arka.entities.VerificationCode;
import com.arka.gateway.CloudStorageGateway;
import com.arka.gateway.EmailGateway;
import com.arka.gateway.repository.VerificationCodeGateway;
import com.arka.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendVerificationCodeEmailUseCaseTest {

    @Mock private UserService userService;
    @Mock private CloudStorageGateway cloudStorageGateway;
    @Mock private EmailGateway emailGateway;
    @Mock private VerificationCodeGateway verificationCodeGateway;
    @Mock private VerificationCodePolicy verificationCodePolicy;

    @InjectMocks
    private SendVerificationCodeEmailUseCase useCase;

    private static final String EMAIL = "john@arka.com";

    @Test
    void shouldCreateNewCodeAndSendEmailWhenCodeDoesNotExist() {
        User user = User.create("johndoe", EMAIL, "pass", null);
        Instant expiration = Instant.now().plusSeconds(900);

        when(userService.findByEmail(EMAIL)).thenReturn(user);
        when(verificationCodePolicy.getExpiration()).thenReturn(expiration);
        when(verificationCodeGateway.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(cloudStorageGateway.getVerificationEmailTemplate()).thenReturn("<html>Template</html>");

        useCase.execute(EMAIL);

        verify(verificationCodeGateway).save(any(VerificationCode.class));
        verify(emailGateway).sendVerificationCode(any(VerificationCodeEmailDto.class));
    }

    @Test
    void shouldRegenerateCodeAndSendEmailWhenCodeAlreadyExists() {
        User user = User.create("johndoe", EMAIL, "pass", null);
        VerificationCode existingCode = VerificationCode.create(user, Instant.now());
        Instant newExpiration = Instant.now().plusSeconds(900);

        when(userService.findByEmail(EMAIL)).thenReturn(user);
        when(verificationCodePolicy.getExpiration()).thenReturn(newExpiration);
        when(verificationCodeGateway.findByUserId(user.getId())).thenReturn(Optional.of(existingCode));
        when(cloudStorageGateway.getVerificationEmailTemplate()).thenReturn("<html>Template</html>");

        useCase.execute(EMAIL);

        verify(verificationCodeGateway).save(existingCode);
        verify(emailGateway).sendVerificationCode(any(VerificationCodeEmailDto.class));
    }
}
