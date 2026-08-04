package com.arka.entities;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordResetTokenTest {

    @Test
    void shouldReturnTrueWhenTokenIsExpired() {
        PasswordResetToken token = PasswordResetToken.create(
                1L, Instant.now().minus(1, ChronoUnit.MINUTES));

        assertThat(token.isExpired()).isTrue();
    }

    @Test
    void shouldReturnFalseWhenTokenIsNotExpired() {
        PasswordResetToken token = PasswordResetToken.create(
                1L, Instant.now().plus(10, ChronoUnit.MINUTES));

        assertThat(token.isExpired()).isFalse();
    }

    @Test
    void shouldGenerateNewTokenAndUpdateExpirationOnReGenerate() {
        PasswordResetToken token = PasswordResetToken.create(
                1L, Instant.now().plus(10, ChronoUnit.MINUTES));

        String originalToken = token.getToken();
        Instant newExpiration = Instant.now().plus(20, ChronoUnit.MINUTES);

        token.reGenerate(newExpiration);

        assertThat(token.getToken()).isNotEqualTo(originalToken);
        assertThat(token.getExpiresAt()).isEqualTo(newExpiration);
        assertThat(token.isUsed()).isFalse();
    }

    @Test
    void shouldResetUsedFlagOnReGenerate() {
        PasswordResetToken token = PasswordResetToken.create(
                1L, Instant.now().plus(10, ChronoUnit.MINUTES));

        token.use();
        assertThat(token.isUsed()).isTrue();

        token.reGenerate(Instant.now().plus(10, ChronoUnit.MINUTES));

        assertThat(token.isUsed()).isFalse();
    }

    @Test
    void shouldMarkTokenAsUsedWhenUseIsCalled() {
        PasswordResetToken token = PasswordResetToken.create(
                1L, Instant.now().plus(10, ChronoUnit.MINUTES));

        assertThat(token.isUsed()).isFalse();

        token.use();

        assertThat(token.isUsed()).isTrue();
    }
}
