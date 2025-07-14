package com.metastore.fileupload.fileupload_backend.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

@Service
public class S3PresignedUrlService {

    private final S3Presigner presigner;
    private final String bucketName = "fileupload-akshitha-2025";

    public S3PresignedUrlService() {
        this.presigner = S3Presigner.builder()
                .region(Region.US_EAST_1) // change to your region
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    // For uploading files
    public URL generatePresignedUrl(String key, String contentType) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(objectRequest)
                .signatureDuration(Duration.ofMinutes(10))
                .build();

        return presigner.presignPutObject(presignRequest).url();
    }

    // 🔽 NEW: For downloading files (view/download in browser)
    public URL generateDownloadPresignedUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofMinutes(60)) // 1 hour
                .build();

        return presigner.presignGetObject(presignRequest).url();
    }
}
