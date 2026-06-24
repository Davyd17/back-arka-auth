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
        this.updatedAt = Instant.now();
    }

    public void assignId(Long id){
        this.id = id;
    }

    public void updateRole(Role role){
        this.role = role;
        this.updatedAt = Instant.now();
    }

    public void updatePassword(String password){
        this.password = password;
        this.updatedAt = Instant.now();
    }

    public void enable(){
        this.enabled = true;
        this.updatedAt = Instant.now();
    }

    public void disable(){
        this.enabled = false;
        this.updatedAt = Instant.now();
    }
}
