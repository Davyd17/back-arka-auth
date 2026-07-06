package com.arka.service;

import com.arka.entities.VerificationCode;
import com.arka.gateway.repository.VerificationCodeGateway;
import com.arka.mapper.VerificationCodeEntityMapper;
import com.arka.repository.VerificationCodeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VerificationCodeServiceAdapter implements VerificationCodeGateway {

    private final VerificationCodeJpaRepository repository;
    private final VerificationCodeEntityMapper mapper;

    @Override
    public VerificationCode save(VerificationCode code) {
        return mapper.toDomain(
                repository.save(mapper.toEntity(code)));
    }

    @Override
    public Optional<VerificationCode> findByUserId(Long userId){
        return repository.findByUserId(userId).map(mapper::toDomain);
    }
}
