package com.arka.service;

import com.arka.exceptions.UserAlreadyExistsException;
import com.arka.entities.User;
import com.arka.exceptions.NotFoundException;
import com.arka.gateway.repository.UserGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {

    private final UserGateway userGateway;

    public User findById(Long userId) {

        return userGateway.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User with id %d not found", userId)
                ));
    }

    public User findByEmail(String email) {
        return userGateway.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User with email %s not found", email)
                ));
    }

    public void checkExistsByEmail(String email) {

        if(userGateway.existsByEmail(email)){
            throw new UserAlreadyExistsException(
                        String.format("User with email %s already exists", email)
                );
        }
    }

    public void checkExistsByUsername(String username) {

        if(userGateway.existsByUsername(username)){
            throw new UserAlreadyExistsException(
                        String.format("User with username %s already exists", username)
                );
        }
    }
}
