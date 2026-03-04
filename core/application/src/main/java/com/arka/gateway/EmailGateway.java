package com.arka.gateway;

import com.arka.model.request.EmailRequest;

public interface EmailGateway {
    void sendEmail(EmailRequest emailRequest);
}
