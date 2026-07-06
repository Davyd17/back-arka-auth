package com.arka.request;

import com.arka.exception.Required;

public record VerificationCodeRequest(
        @Required(field = "verificationCode") String verificationCode
) {
}
