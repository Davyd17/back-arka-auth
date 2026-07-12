package com.arka.service;

import com.arka.entities.PasswordResetToken;
import com.arka.gateway.repository.PasswordResetTokenGateway;
import com.arka.mapper.PasswordResetTokenEntityMapper;
import com.arka.repository.PasswordResetTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PasswordResetTokenAdapter implements PasswordResetTokenGateway {

    private final PasswordResetTokenJpaRepository repository;
    private final PasswordResetTokenEntityMapper mapper;

    @Override
    public Optional<PasswordResetToken> findByUserId(Long id) {
        return repository.findByUserId(id).map(mapper::toDomain);
    }

    @Override
    public PasswordResetToken save(PasswordResetToken token) {
        return mapper.toDomain(repository.save(mapper.toEntity(token)));
    }
}
