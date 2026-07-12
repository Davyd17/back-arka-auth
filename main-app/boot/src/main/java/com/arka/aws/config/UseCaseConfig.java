package com.arka.aws.config;

import com.arka.dto.value.PasswordResetTokenPolicy;
import com.arka.dto.value.VerificationCodePolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * Spring configuration class responsible for assembling domain use cases
 * and binding infrastructure configuration to domain policy objects.
 * <p>
 * Scans for classes matching UseCase and Service naming conventions
 * to register them as Spring-managed beans without requiring individual
 * Spring annotations in the domain layer.
 */
@Configuration
@ComponentScan(basePackages = {"com.arka"},
    includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX,pattern = "^.+UseCase$"),
            @ComponentScan.Filter(type = FilterType.REGEX,pattern = "^.+Service")
    }, useDefaultFilters = false)
public class UseCaseConfig {

    @Value("${notifications.email-settings.verification-code.code-expiration-ms}")
    private long codeExpirationAtMs;

    @Value("${notifications.email-settings.password-reset.token-expiration-ms}")
    private long tokenExpirationMs;

    /**
     * Constructs the {@link VerificationCodePolicy} domain object from
     * externalized configuration, keeping Spring annotations out of the domain layer.
     *
     * @return a configured {@link VerificationCodePolicy} instance
     */
    @Bean
    public VerificationCodePolicy verificationCodePolicy(){
        return new VerificationCodePolicy(codeExpirationAtMs);
    }


    /**
     * Constructs the {@link PasswordResetTokenPolicy} domain object from
     * externalized configuration, keeping Spring annotations out of the domain layer.
     *
     * @return a configured {@link PasswordResetTokenPolicy} instance
     */
    @Bean
    public PasswordResetTokenPolicy passwordResetTokenPolicy(){
        return new PasswordResetTokenPolicy(tokenExpirationMs);
    }
}
