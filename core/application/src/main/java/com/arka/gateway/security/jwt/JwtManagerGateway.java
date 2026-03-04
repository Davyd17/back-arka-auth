package com.arka.gateway.security.jwt;

import com.arka.dto.value.SecurityUserDto;

import java.util.Date;

public interface JwtManagerGateway {

    boolean isTokenValid(String token, SecurityUserDto user);

    String extractEmail(String token);

    Date extractExpirationDate(String token);
}
