package com.metastore.fileupload.fileupload_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

import java.util.stream.Collectors;

@RestController
public class TestS3Controller {

    @Autowired
    private S3Client s3Client;

    @GetMapping("/test-s3")
    public String testS3() {
        ListBucketsResponse bucketsResponse = s3Client.listBuckets();
        return bucketsResponse.buckets()
                .stream()
                .map(b -> b.name())
                .collect(Collectors.joining(", "));
    }
}
