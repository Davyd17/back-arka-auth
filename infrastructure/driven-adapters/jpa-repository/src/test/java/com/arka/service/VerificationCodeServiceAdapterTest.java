package com.arka.service;

import com.arka.entities.User;
import com.arka.entities.VerificationCode;
import com.arka.mapper.UserEntityMapper;
import com.arka.mapper.UserEntityMapperImpl;
import com.arka.mapper.VerificationCodeEntityMapper;
import com.arka.mapper.VerificationCodeEntityMapperImpl;
import com.arka.repository.UserRepository;
import com.arka.repository.VerificationCodeJpaRepository;
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
@Import({VerificationCodeServiceAdapter.class,
        VerificationCodeEntityMapperImpl.class,
        UserEntityMapperImpl.class})
class VerificationCodeServiceAdapterTest {

    @Autowired
    private VerificationCodeServiceAdapter verificationCodeServiceAdapter;

    @Autowired
    private VerificationCodeJpaRepository verificationCodeJpaRepository;

    @Autowired
    private VerificationCodeEntityMapper verificationCodeEntityMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Test
    void shouldSaveNewVerificationCode() {

        User existingUser = userEntityMapper.toDomain(
                userRepository.findById(1L).orElseThrow());

        VerificationCode newCode = VerificationCode.
                create(existingUser, Instant.now().plus(15, ChronoUnit.MINUTES));

        VerificationCode savedCode = verificationCodeServiceAdapter.save(newCode);

        assertThat(savedCode.getId()).isNotNull();
        assertThat(savedCode.getCode()).isEqualTo(newCode.getCode());
        assertThat(savedCode.getUser().getId()).isEqualTo(existingUser.getId());
        assertThat(savedCode.isUsed()).isFalse();
    }

    @Test
    void shouldUpdateVerificationCode() {

        VerificationCode existingCode = verificationCodeEntityMapper.toDomain(
                verificationCodeJpaRepository.findByUserId(2L).orElseThrow());

        existingCode.use();
        VerificationCode updatedCode = verificationCodeServiceAdapter.save(existingCode);

        assertThat(updatedCode.getId()).isEqualTo(existingCode.getId());
        assertThat(updatedCode.isUsed()).isTrue();
    }
}
