package com.arka.gateway.security;

public interface PasswordEncryptionGateway {

    String encodePassword(String password);

    boolean matches(String rawPassword, String encodedPassword);

}
