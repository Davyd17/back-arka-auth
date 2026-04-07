package com.arka.service;

import com.arka.mapper.UserEntityMapper;
import com.arka.model.User;
import com.arka.gateway.repository.UserGateway;
import com.arka.repository.UserRepository;
import com.arka.tables.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceAdapter implements UserGateway {

    private final UserRepository userRepository;
    private final UserEntityMapper mapper;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public User save(User user) {

        UserEntity entity = mapper.toEntity(user);

        System.out.println("DEBUG 2 - Entity User: " + entity.getUsername());
        System.out.println("DEBUG 2 - Entity User: " + entity.getEmail());

        return mapper.toDomain(userRepository.save(entity));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

}
