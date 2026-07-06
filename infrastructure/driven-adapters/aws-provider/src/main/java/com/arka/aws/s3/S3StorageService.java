package com.arka.aws.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.nio.ByteBuffer;

@Service
@RequiredArgsConstructor
public class S3StorageService {
    
    private final S3Client s3Client;

    public ResponseBytes<GetObjectResponse> download(String keyName, String bucket) throws S3Exception {

        return s3Client.getObjectAsBytes(
                GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(keyName)
                        .build());
    }
}
