package com.arka.entities;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class VerificationCodeTest {

    @Test
    void shouldReturnTrueWhenCodeIsExpired() {
        VerificationCode code = VerificationCode.create(
                null, Instant.now().minus(1, ChronoUnit.MINUTES));

        assertThat(code.isExpired()).isTrue();
    }

    @Test
    void shouldReturnFalseWhenCodeIsNotExpired() {
        VerificationCode code = VerificationCode.create(
                null, Instant.now().plus(15, ChronoUnit.MINUTES));

        assertThat(code.isExpired()).isFalse();
    }

    @Test
    void shouldGenerateNewCodeAndUpdateExpirationOnReGenerate() {
        VerificationCode code = VerificationCode.create(
                null, Instant.now().plus(15, ChronoUnit.MINUTES));

        String originalCode = code.getCode();
        Instant newExpiration = Instant.now().plus(30, ChronoUnit.MINUTES);

        code.reGenerate(newExpiration);

        assertThat(code.getCode()).isNotEqualTo(originalCode);
        assertThat(code.getExpiresAt()).isEqualTo(newExpiration);
    }

    @Test
    void shouldMarkCodeAsUsedWhenUseIsCalled() {
        VerificationCode code = VerificationCode.create(
                null, Instant.now().plus(15, ChronoUnit.MINUTES));

        assertThat(code.isUsed()).isFalse();

        code.use();

        assertThat(code.isUsed()).isTrue();
    }

}
