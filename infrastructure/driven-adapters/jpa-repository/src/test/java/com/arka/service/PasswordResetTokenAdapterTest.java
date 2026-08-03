package com.arka.service;

import com.arka.entities.PasswordResetToken;
import com.arka.mapper.PasswordResetTokenEntityMapper;
import com.arka.mapper.PasswordResetTokenEntityMapperImpl;
import com.arka.repository.PasswordResetTokenJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({PasswordResetTokenAdapter.class, PasswordResetTokenEntityMapperImpl.class})
class PasswordResetTokenAdapterTest {

    @Autowired
    private PasswordResetTokenAdapter passwordResetTokenAdapter;

    @Autowired
    private PasswordResetTokenJpaRepository repository;

    @Autowired
    private PasswordResetTokenEntityMapper mapper;

    @Test
    void shouldSaveNewPasswordResetToken() {
        PasswordResetToken newToken = PasswordResetToken.create(
                1L, Instant.now().plus(2, ChronoUnit.HOURS));

        PasswordResetToken savedToken = passwordResetTokenAdapter.save(newToken);

        assertThat(savedToken.getId()).isNotNull();
        assertThat(savedToken.getToken()).isEqualTo(newToken.getToken());
        assertThat(savedToken.getUserId()).isEqualTo(1L);
        assertThat(savedToken.isUsed()).isFalse();
    }

    @Test
    void shouldUpdateSeededPasswordResetToken() {

        PasswordResetToken existingToken = mapper.toDomain(
                repository.findByUserId(2L).orElseThrow());

        existingToken.use();
        PasswordResetToken updatedToken = passwordResetTokenAdapter.save(existingToken);

        assertThat(updatedToken.getId()).isEqualTo(existingToken.getId());
        assertThat(updatedToken.isUsed()).isTrue();
    }
}
