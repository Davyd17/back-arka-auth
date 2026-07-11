package com.arka.aws.ses;

import com.arka.dto.value.PasswordResetTokenEmailDto;

public class TemplateProcessor {

    public static String resolveVerificationEmail(String template,
                                                  String username,
                                                  String code){

        return template
                .replace("{{userName}}", username)
                .replace("{{verificationCode}}", code);
    }

    public static String resolvePasswordResetEmail(PasswordResetTokenEmailDto dto, String baseUrl) {
        return dto.templateBody()
                .replace("{{userName}}", dto.username())
                .replace("{{baseUrl}}", baseUrl)
                .replace("{{resetToken}}", dto.token());
    }
}
