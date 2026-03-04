package com.arka.gateway.security.jwt;

import com.arka.model.User;

public interface JwtGeneratorGateway {

    String generateToken(User user);
}
