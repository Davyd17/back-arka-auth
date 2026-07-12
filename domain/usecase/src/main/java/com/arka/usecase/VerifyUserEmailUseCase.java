package com.arka.usecase;

import com.arka.entities.User;
import com.arka.entities.VerificationCode;
import com.arka.exceptions.InvalidCodeException;
import com.arka.exceptions.NotFountException;
import com.arka.gateway.repository.UserGateway;
import com.arka.gateway.repository.VerificationCodeGateway;
import com.arka.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class VerifyUserEmailUseCase {

    private final VerificationCodeGateway verificationCodeGateway;
    private final UserService userService;
    private final UserGateway userGateway;

    public void execute(String email, String code) {

        User foundUser = userService.findByEmail(email);

        VerificationCode verificationCode =
                verificationCodeGateway.findByUserId(foundUser.getId())
                        .orElseThrow(() -> new NotFountException("Invalid verification code"));

        verifyCode(verificationCode, code);
        foundUser.verify();
        userGateway.save(foundUser);
    }

    private void verifyCode(VerificationCode code, String inputCode) {

        if (code.isExpired() || !Objects.equals(code.getCode(), inputCode) || code.isUsed())
            throw new InvalidCodeException("Expired or invalid code");

        code.use();
        verificationCodeGateway.save(code);
    }
}
