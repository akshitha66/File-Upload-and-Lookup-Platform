// FileUpload.jsx
import React, { useState } from "react";

async function getPresignedUrl(fileName, fileType) {
  const response = await fetch("http://localhost:8080/api/s3/generatePresignedUrl", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ fileName, fileType }),
  });
  if (!response.ok) {
    throw new Error("Failed to get presigned URL");
  }
  const data = await response.json();
  return data.url;
}

async function uploadFileToS3(file, presignedUrl) {
  const response = await fetch(presignedUrl, {
    method: "PUT",
    headers: { "Content-Type": file.type },
    body: file,
  });
  if (!response.ok) {
    throw new Error("Failed to upload file to S3");
  }
}

async function saveFileMetadata(fileName, fileUrl) {
  const response = await fetch("http://localhost:8080/api/files/saveMetadata", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ fileName, fileUrl }),
  });
  if (!response.ok) {
    throw new Error("Failed to save file metadata");
  }
}

export default function FileUpload() {
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploading, setUploading] = useState(false);

  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
  };

  const handleUpload = async () => {
    if (!selectedFile) {
      alert("Please select a file first");
      return;
    }
    setUploading(true);
    try {
      // 1. Get presigned URL
      const presignedUrl = await getPresignedUrl(selectedFile.name, selectedFile.type);

      // 2. Upload file to S3
      await uploadFileToS3(selectedFile, presignedUrl);

      // 3. Save metadata to backend (strip query params from presigned URL)
      const cleanUrl = presignedUrl.split("?")[0];
      await saveFileMetadata(selectedFile.name, cleanUrl);

      alert("File uploaded and metadata saved!");
      setSelectedFile(null); // reset file input if you want
    } catch (error) {
      console.error("Upload error:", error);
      alert("Upload failed. See console for details.");
    } finally {
      setUploading(false);
    }
  };

  return (
    <div>
      <input type="file" onChange={handleFileChange} />
      <button onClick={handleUpload} disabled={uploading}>
        {uploading ? "Uploading..." : "Upload File"}
      </button>
    </div>
  );
}
