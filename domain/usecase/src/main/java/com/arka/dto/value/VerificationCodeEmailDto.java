package com.arka.dto.value;

public record VerificationCodeEmailDto (
        String recipient,
        String username,
        String templateBody,
        String code
){
}
