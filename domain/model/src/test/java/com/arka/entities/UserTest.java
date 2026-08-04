package com.arka.entities;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    private User buildUser() {
        return User.create("johndoe", "john@arka.com", "password123", null);
    }

    @Test
    void shouldCreateUserWithDefaultValues() {
        User user = buildUser();

        assertThat(user.isVerified()).isFalse();
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    void shouldVerifyUser() {
        User user = buildUser();

        user.verify();

        assertThat(user.isVerified()).isTrue();
    }

    @Test
    void shouldThrowWhenVerifyingAlreadyVerifiedUser() {
        User user = buildUser();
        user.verify();

        assertThatThrownBy(user::verify)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User already verified");
    }

    @Test
    void shouldDisableUser() {
        User user = buildUser();

        user.disable();

        assertThat(user.isEnabled()).isFalse();
    }

    @Test
    void shouldEnableUser() {
        User user = buildUser();
        user.disable();

        user.enable();

        assertThat(user.isEnabled()).isTrue();
    }
}
