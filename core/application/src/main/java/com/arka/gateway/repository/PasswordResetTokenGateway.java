package com.arka.gateway.repository;

import com.arka.model.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenGateway {
    Optional<PasswordResetToken> findByToken(String token);
    PasswordResetToken save(PasswordResetToken passwordResetToken);
    void delete(PasswordResetToken passwordResetToken);
    void deleteByUser(String email);
}
