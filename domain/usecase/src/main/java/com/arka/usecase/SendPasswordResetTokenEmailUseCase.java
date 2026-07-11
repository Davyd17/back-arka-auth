package com.arka.usecase;

import com.arka.dto.value.PasswordResetTokenEmailDto;
import com.arka.dto.value.PasswordResetTokenPolicy;
import com.arka.entities.PasswordResetToken;
import com.arka.entities.User;
import com.arka.gateway.CloudStorageGateway;
import com.arka.gateway.EmailGateway;
import com.arka.gateway.repository.PasswordResetTokenGateway;
import com.arka.service.UserService;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
public class SendPasswordResetTokenEmailUseCase {

    private final UserService userService;
    private final PasswordResetTokenGateway passwordResetTokenGateway;
    private final PasswordResetTokenPolicy passwordResetPolicy;
    private final EmailGateway emailGateway;
    private final CloudStorageGateway storageGateway;

    public void execute(String recipient){

        User foundUser = userService.findByEmail(recipient);
        Instant expiration = passwordResetPolicy.getExpiration();


        PasswordResetToken token = passwordResetTokenGateway
                .findByUserId(foundUser.getId())
                .map(foundToken -> {

                    foundToken.reGenerate(expiration);
                    return foundToken;

                }).orElseGet(() -> PasswordResetToken.create(foundUser.getId(), expiration));

        emailGateway.sendPasswordResetToken(
                buildEmail(recipient, foundUser.getUsername(), token.getToken()));
    }

    private PasswordResetTokenEmailDto buildEmail(String recipient, String username, String token){

        String template = storageGateway.getPasswordResetEmailTemplate();

        return new PasswordResetTokenEmailDto(
                recipient, username, template,token);
    }
}
