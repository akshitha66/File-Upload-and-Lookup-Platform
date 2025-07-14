import React, { useState } from "react";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

async function getPresignedUrl(fileName, fileType) {
  const response = await fetch("http://localhost:8080/api/s3/generatePresignedUrl", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ fileName, fileType }),
  });
  if (!response.ok) throw new Error("Failed to get presigned URL");
  const data = await response.json();
  return data.url;
}

async function uploadFileToS3(file, presignedUrl) {
  const response = await fetch(presignedUrl, {
    method: "PUT",
    headers: { "Content-Type": file.type },
    body: file,
  });
  if (!response.ok) throw new Error("Failed to upload file to S3");
}

async function saveFileMetadata(fileName, fileUrl) {
  const response = await fetch("http://localhost:8080/api/files/saveMetadata", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ fileName, fileUrl }),
  });
  if (!response.ok) throw new Error("Failed to save file metadata");
}

export default function FileUpload() {
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploading, setUploading] = useState(false);

  const handleFileChange = (e) => setSelectedFile(e.target.files[0]);

  const handleUpload = async () => {
    if (!selectedFile) {
      toast.warn("Please select a file first");
      return;
    }
    setUploading(true);
    try {
      const presignedUrl = await getPresignedUrl(selectedFile.name, selectedFile.type);
      await uploadFileToS3(selectedFile, presignedUrl);
      const cleanUrl = presignedUrl.split("?")[0];
      await saveFileMetadata(selectedFile.name, cleanUrl);

      toast.success("File uploaded and metadata saved!");
      setSelectedFile(null);
    } catch (error) {
      console.error(error);
      toast.error("Upload failed. Check console.");
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
      <ToastContainer position="top-right" autoClose={3000} />
    </div>
  );
}
