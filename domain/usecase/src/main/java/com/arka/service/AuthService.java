package com.arka.service;

import com.arka.dto.value.TokenDetailsDto;
import com.arka.gateway.security.PasswordEncryptionGateway;
import com.arka.gateway.security.jwt.JwtGeneratorGateway;
import com.arka.gateway.security.jwt.JwtManagerGateway;
import com.arka.entities.User;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
public class AuthService {

    private final JwtGeneratorGateway jwtGenerator;
    private final JwtManagerGateway jwtManager;
    private final PasswordEncryptionGateway passwordEncryption;

    public TokenDetailsDto buildAuthToken(User newUser) {

        String token = jwtGenerator.generateToken(newUser);

        Instant expiresOn =
                jwtManager.extractExpirationDate(token).toInstant();

        return new TokenDetailsDto(
                token,
                "Bearer",
                expiresOn
        );
    }

    public boolean passwordsMatches(String storedHash, String rawPassword) {
        return passwordEncryption.matches(rawPassword, storedHash);
    }
}
