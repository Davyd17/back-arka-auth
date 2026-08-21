package com.arka.usecase;

import com.arka.dto.input.PasswordResetInput;
import com.arka.entities.PasswordResetToken;
import com.arka.entities.User;
import com.arka.exceptions.InvalidTokenException;
import com.arka.gateway.repository.PasswordResetTokenGateway;
import com.arka.gateway.repository.UserGateway;
import com.arka.gateway.security.PasswordEncryptionGateway;
import com.arka.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class PasswordResetUseCase {

    private final PasswordResetTokenGateway passwordResetTokenGateway;
    private final UserService userService;
    private final UserGateway userGateway;
    private final PasswordEncryptionGateway passwordEncryptionGateway;

    public void execute(PasswordResetInput input) {

        PasswordResetToken resetToken = passwordResetTokenGateway
                .findByToken(input.token())
                .orElseThrow(InvalidTokenException::new);

        verifyToken(resetToken, input.token());

        updateUserWithNewPassword(resetToken.getUserId(), input.newPassword());

        resetToken.use();
        passwordResetTokenGateway.save(resetToken);

    }

    private void verifyToken(PasswordResetToken token, String inputToken) {

        if (!Objects.equals(token.getToken(), inputToken)
                || token.isExpired()
                || token.isUsed()) {

            throw new InvalidTokenException();
        }
    }

    private void updateUserWithNewPassword(Long userId, String newPassword){

        User foundUser = userService.findById(userId);
        foundUser.updatePassword(passwordEncryptionGateway
                .encodePassword(newPassword));

        userGateway.save(foundUser);
    }
}
