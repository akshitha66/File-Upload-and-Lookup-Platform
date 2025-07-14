package com.metastore.fileupload.fileupload_backend.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@Service
public class S3DeleteService {

    private final S3Client s3Client;
    private final String bucketName = "fileupload-akshitha-2025";

    public S3DeleteService() {
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_1) // your region
                .build();
    }

    public void deleteFile(String key) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.deleteObject(request);
    }
}
