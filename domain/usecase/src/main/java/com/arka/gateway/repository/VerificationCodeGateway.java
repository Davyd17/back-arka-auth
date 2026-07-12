package com.arka.gateway.repository;

import com.arka.entities.VerificationCode;

import java.util.Optional;

public interface VerificationCodeGateway {

    VerificationCode save(VerificationCode code);

    Optional<VerificationCode> findByUserId(Long userId);
}
