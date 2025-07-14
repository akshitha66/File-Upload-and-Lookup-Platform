package com.metastore.fileupload.fileupload_backend.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import com.metastore.fileupload.fileupload_backend.model.FileMetadataDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DynamoDBService {

    private DynamoDbClient dynamoDbClient;
    private final String tableName = "FileMetadata";

    @PostConstruct
    public void init() {
        dynamoDbClient = DynamoDbClient.builder()
                .region(Region.US_EAST_1)  // your region
                .build();
    }

    public void saveMetadata(String fileName, String fileUrl, String timestamp) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("fileName", AttributeValue.builder().s(fileName).build());
        item.put("fileUrl", AttributeValue.builder().s(fileUrl).build());
        item.put("timestamp", AttributeValue.builder().s(timestamp).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
    }

    public List<FileMetadataDto> getAllMetadata() {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(tableName)
                .build();

        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);

        List<FileMetadataDto> metadataList = new ArrayList<>();

        for (Map<String, AttributeValue> item : scanResponse.items()) {
            FileMetadataDto dto = new FileMetadataDto();
            dto.setFileName(item.get("fileName") != null ? item.get("fileName").s() : "");
            dto.setFileUrl(item.get("fileUrl") != null ? item.get("fileUrl").s() : "");
            dto.setTimestamp(item.get("timestamp") != null ? item.get("timestamp").s() : "");
            metadataList.add(dto);
        }

        return metadataList;
    }

    public void deleteMetadata(String fileName) {
        Map<String, AttributeValue> keyToDelete = new HashMap<>();
        keyToDelete.put("fileName", AttributeValue.builder().s(fileName).build());

        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(keyToDelete)
                .build();

        dynamoDbClient.deleteItem(request);
    }

}
