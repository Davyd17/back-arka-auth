package com.arka.service;

import com.arka.entities.Role;
import com.arka.entities.User;
import com.arka.mapper.*;
import com.arka.repository.RoleJpaRepository;
import com.arka.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({UserServiceAdapter.class, UserEntityMapperImpl.class, RoleEntityMapperImpl.class})
class UserServiceAdapterTest {

    @Autowired
    private UserServiceAdapter userServiceAdapter;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private RoleEntityMapper roleEntityMapper;

    @Autowired
    private RoleJpaRepository roleJpaRepository;

    private Role role;

    @BeforeEach
    void setUp(){
        role = roleEntityMapper.toDomain(
                roleJpaRepository.findById(1L).orElseThrow());
    }

    @Test
    void shouldSaveNewUser() {

        User newUser = User.create(
                "johndoe", "john@arka.com", "securePassword123", role);

        User savedUser = userServiceAdapter.save(newUser);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("johndoe");
        assertThat(savedUser.isVerified()).isFalse();
        assertThat(savedUser.isEnabled()).isTrue();
    }

    @Test
    void shouldUpdateSeededUser() {

        User existingUser = userEntityMapper.toDomain(
                userRepository.findById(1L).orElseThrow());

        existingUser.disable();
        User updatedUser = userServiceAdapter.save(existingUser);

        assertThat(updatedUser.getId()).isEqualTo(1L);
        assertThat(updatedUser.getUsername()).isEqualTo(existingUser.getUsername());
        assertThat(updatedUser.isEnabled()).isFalse();
    }

}
