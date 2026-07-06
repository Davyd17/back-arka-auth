package com.arka.gateway;

import com.arka.dto.value.VerificationCodeEmailDto;

public interface EmailGateway {

    void sendVerificationCode(VerificationCodeEmailDto codeEmail);
}
