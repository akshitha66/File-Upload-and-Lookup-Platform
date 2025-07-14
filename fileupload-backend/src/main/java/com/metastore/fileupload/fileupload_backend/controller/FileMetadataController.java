package com.metastore.fileupload.fileupload_backend.controller;

import com.metastore.fileupload.fileupload_backend.model.FileMetadataDto;
import com.metastore.fileupload.fileupload_backend.service.DynamoDBService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileMetadataController {

    private final DynamoDBService dynamoDBService;

    public FileMetadataController(DynamoDBService dynamoDBService) {
        this.dynamoDBService = dynamoDBService;
    }

    @PostMapping("/saveMetadata")
    public ResponseEntity<String> saveFileMetadata(@RequestBody FileMetadataDto metadataDto) {
        metadataDto.setTimestamp(Instant.now().toString());

        // Save metadata to DynamoDB
        dynamoDBService.saveMetadata(metadataDto.getFileName(), metadataDto.getFileUrl(), metadataDto.getTimestamp());

        return ResponseEntity.ok("Metadata saved successfully");
    }

    @GetMapping("/allMetadata")
    public ResponseEntity<List<FileMetadataDto>> getAllMetadata() {
        List<FileMetadataDto> metadataList = dynamoDBService.getAllMetadata();
        return ResponseEntity.ok(metadataList);
    }

}
