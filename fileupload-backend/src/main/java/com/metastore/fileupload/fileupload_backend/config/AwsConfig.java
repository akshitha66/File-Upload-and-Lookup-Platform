package com.metastore.fileupload.fileupload_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {

    @Value("${aws.s3.region}")
    private String region;

    @Bean
    public S3Client s3Client() {
        System.out.println("AWS Region from properties: " + region);
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())  // uses IAM Role or env vars
                .build();
    }
}
