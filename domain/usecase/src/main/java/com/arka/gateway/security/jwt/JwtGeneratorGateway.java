package com.arka.gateway.security.jwt;

import com.arka.entities.User;

public interface JwtGeneratorGateway {

    String generateToken(User user);
}
