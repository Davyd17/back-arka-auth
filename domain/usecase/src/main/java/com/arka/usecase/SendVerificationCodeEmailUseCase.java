package com.arka.usecase;

import com.arka.dto.value.VerificationCodeEmailDto;
import com.arka.dto.value.VerificationCodePolicy;
import com.arka.entities.User;
import com.arka.entities.VerificationCode;
import com.arka.gateway.CloudStorageGateway;
import com.arka.gateway.EmailGateway;
import com.arka.gateway.repository.VerificationCodeGateway;
import com.arka.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public class SendVerificationCodeEmailUseCase {

    private final UserService userService;
    private final CloudStorageGateway cloudStorageGateway;
    private final EmailGateway emailGateway;
    private final VerificationCodeGateway verificationCodeGateway;

    private final VerificationCodePolicy verificationCodePolicy;

    public void execute(String email) {

        User user = userService.findByEmail(email);
        Instant expiration = verificationCodePolicy.getExpiration();

        VerificationCode verificationCode = verificationCodeGateway.findByUserId(user.getId())
                .map(code -> {
                    code.reGenerate(expiration);
                    return code;
                }).orElseGet(() -> VerificationCode.create(user, expiration));

        VerificationCodeEmailDto verificationCodeEmail =
                buildEmail(user.getEmail(), user.getUsername(), verificationCode.getCode());

        verificationCodeGateway.save(verificationCode);
        emailGateway.sendVerificationCode(verificationCodeEmail);
    }

    private VerificationCodeEmailDto buildEmail(String recipient, String username, String code) {

        String template = cloudStorageGateway
                .getVerificationEmailTemplate();

        return new VerificationCodeEmailDto(recipient, username, template, code);
    }
}

