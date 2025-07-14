package com.metastore.fileupload.fileupload_backend.model;

public class FileMetadataDto {
    private String fileName;
    private String fileUrl;
    private String timestamp;

    // Getters and setters for all fields

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileUrl() {
        return fileUrl;
    }
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
