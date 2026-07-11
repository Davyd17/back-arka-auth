package com.arka.gateway.repository;

import com.arka.entities.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenGateway {

    Optional<PasswordResetToken> findByUserId(Long id);

}
