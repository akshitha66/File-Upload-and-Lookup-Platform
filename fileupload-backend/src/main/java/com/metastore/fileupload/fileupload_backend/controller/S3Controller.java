package com.metastore.fileupload.fileupload_backend.controller;

import com.metastore.fileupload.fileupload_backend.service.S3PresignedUrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.Map;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3PresignedUrlService presignedUrlService;

    public S3Controller(S3PresignedUrlService presignedUrlService) {
        this.presignedUrlService = presignedUrlService;
    }

    // 🔼 Already used: for uploading
    @PostMapping("/generatePresignedUrl")
    public Map<String, String> generatePresignedUrl(@RequestBody Map<String, String> request) {
        String fileName = request.get("fileName");
        String contentType = request.get("contentType");

        URL presignedUrl = presignedUrlService.generatePresignedUrl(fileName, contentType);

        return Map.of("url", presignedUrl.toString());
    }

    // 🔽 NEW: for downloading (lookup/view files)
    @GetMapping("/downloadPresignedUrl")
    public ResponseEntity<Map<String, String>> getDownloadPresignedUrl(@RequestParam String fileName) {
        URL presignedUrl = presignedUrlService.generateDownloadPresignedUrl(fileName);
        return ResponseEntity.ok(Map.of("url", presignedUrl.toString()));
    }
}
