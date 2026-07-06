package com.arka.aws.s3;

import com.arka.exceptions.TemplateStorageException;
import com.arka.gateway.CloudStorageGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Adapter implements CloudStorageGateway {

    private final S3StorageService s3StorageService;

    @Value("${cloud-provider.aws.s3.bucket-name}")
    private String bucket;

    @Value("${cloud-provider.aws.s3.objects.email-verification-template}")
    private String verificationEmailTemplate;

    public String getVerificationEmailTemplate() {

        try {
            return s3StorageService.download(
                    verificationEmailTemplate, bucket).asUtf8String();

        } catch (S3Exception e) {
            log.error("Failed to fetch template from S3: {}", e.getMessage());
            throw new TemplateStorageException("Could not download verification email template", e);
        }
    }
}
