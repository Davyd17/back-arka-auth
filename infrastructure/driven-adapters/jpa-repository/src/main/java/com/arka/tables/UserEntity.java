package com.arka.tables;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true,nullable = false)
    private String username;

    @Column(name = "email", unique = true,nullable = false)
    private String email;

    @Column(name = "password",  nullable = false)
    private String password;

    @Column(name = "enabled",nullable = false)
    private boolean enabled;

    @CreationTimestamp
    @Column(name = "createdAt",nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt",nullable = true)
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;
}
