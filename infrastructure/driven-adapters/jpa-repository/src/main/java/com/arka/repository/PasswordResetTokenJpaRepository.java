package com.arka.repository;

import com.arka.tables.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenJpaRepository
        extends JpaRepository<PasswordResetTokenEntity, Long> {

    Optional<PasswordResetTokenEntity> findByToken(String token);

    Optional<PasswordResetTokenEntity> findByUserId(Long id);
}
