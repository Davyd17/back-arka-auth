package com.arka.tables;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "verification_codes")
public class VerificationCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean isUsed = false;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserEntity user;
}
