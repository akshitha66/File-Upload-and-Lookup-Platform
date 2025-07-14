package com.metastore.fileupload.fileupload_backend.controller;

import com.metastore.fileupload.fileupload_backend.service.DynamoDBService;
import com.metastore.fileupload.fileupload_backend.service.S3DeleteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files")
public class FileManagementController {

    private final S3DeleteService s3DeleteService;
    private final DynamoDBService dynamoDBService;

    public FileManagementController(S3DeleteService s3DeleteService, DynamoDBService dynamoDBService) {
        this.s3DeleteService = s3DeleteService;
        this.dynamoDBService = dynamoDBService;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam String fileName) {
        try {
            s3DeleteService.deleteFile(fileName);
            dynamoDBService.deleteMetadata(fileName);
            return ResponseEntity.ok("File and metadata deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting file: " + e.getMessage());
        }
    }
}
