package com.arka.gateway.repository;

import com.arka.model.User;

import java.util.Optional;

public interface UserGateway {

    Optional<User> findById(Long id);

    Optional<User> findUserByEmail(String email);

    User save(User user);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
