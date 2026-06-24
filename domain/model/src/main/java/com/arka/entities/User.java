package com.arka.entities;

import lombok.*;

import java.time.Instant;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;

    public static User create(String username,
                              String email,
                              String password,
                              Role role){

        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .enabled(true)
                .createdAt(Instant.now())
                .build();
    }

    public void assignRole(Role role){
        this.role = role;
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void enable(){
        this.enabled = true;
    }

    public void disable(){
        this.enabled = false;
    }
}
