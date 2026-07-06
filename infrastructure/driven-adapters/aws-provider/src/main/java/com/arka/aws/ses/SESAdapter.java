package com.arka.aws.ses;

import com.arka.dto.value.VerificationCodeEmailDto;
import com.arka.gateway.EmailGateway;
import com.arka.valueobjects.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SESAdapter implements EmailGateway {

    private final SESEmailService sesEmailService;

    @Value("${notifications.email-settings.verification-code.subject}")
    private String subject;

    @Override
    public void sendVerificationCode(VerificationCodeEmailDto codeEmail) {

        String processedTemplate = TemplateProcessor
                .resolveVerificationEmail(
                        codeEmail.templateBody(),
                        codeEmail.username(),
                        codeEmail.code());

        sesEmailService.send(EmailMessage
                .create(codeEmail.recipient(), subject, processedTemplate));
    }
}
