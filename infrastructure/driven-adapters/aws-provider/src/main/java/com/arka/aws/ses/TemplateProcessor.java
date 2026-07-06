package com.arka.aws.ses;

public class TemplateProcessor {

    public static String resolveVerificationEmail(String template,
                                                  String username,
                                                  String code){

        return template
                .replace("{{userName}}", username)
                .replace("{{verificationCode}}", code);
    }
}
