import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import ReactModal from "react-modal";
import "react-toastify/dist/ReactToastify.css";

ReactModal.setAppElement("#root"); // Adjust if your root element id is different

export default function UploadedFilesList() {
  const [files, setFiles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [downloadLinks, setDownloadLinks] = useState({});
  const [modalOpen, setModalOpen] = useState(false);
  const [fileToDelete, setFileToDelete] = useState(null);

  useEffect(() => {
    fetchFiles();
  }, []);

  const fetchFiles = () => {
    setLoading(true);
    fetch("http://localhost:8080/api/files/allMetadata")
      .then((res) => res.json())
      .then((data) => {
        setFiles(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Failed to fetch files", err);
        toast.error("Failed to fetch files");
        setLoading(false);
      });
  };

  const handleFileClick = async (fileName) => {
    try {
      if (downloadLinks[fileName]) {
        window.open(downloadLinks[fileName], "_blank");
        return;
      }

      const response = await fetch(
        `http://localhost:8080/api/s3/downloadPresignedUrl?fileName=${encodeURIComponent(fileName)}`
      );
      const data = await response.json();

      if (data.url) {
        setDownloadLinks((prev) => ({ ...prev, [fileName]: data.url }));
        window.open(data.url, "_blank");
      } else {
        toast.error("Could not generate download URL");
      }
    } catch (error) {
      console.error("Error generating download URL", error);
      toast.error("Failed to access file");
    }
  };

  const openDeleteModal = (fileName) => {
    setFileToDelete(fileName);
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
    setFileToDelete(null);
  };

  const confirmDelete = async () => {
    if (!fileToDelete) return;

    try {
      const res = await fetch(`http://localhost:8080/api/files/delete?fileName=${encodeURIComponent(fileToDelete)}`, {
        method: "DELETE",
      });

      if (res.ok) {
        toast.success("Deleted successfully");
        fetchFiles();
        setDownloadLinks((prev) => {
          const copy = { ...prev };
          delete copy[fileToDelete];
          return copy;
        });
      } else {
        toast.error("Failed to delete file");
      }
    } catch (err) {
      console.error("Delete error:", err);
      toast.error("Error deleting file");
    } finally {
      closeModal();
    }
  };

  return (
    <div>
      <h2>Uploaded Files</h2>
      <Link to="/">
        <button>Back to Upload</button>
      </Link>

      {loading ? (
        <p>Loading...</p>
      ) : files.length === 0 ? (
        <p>No uploaded files found.</p>
      ) : (
        <ul>
          {files.map((file, i) => {
            if (!file.fileName) return null;

            const date = new Date(file.timestamp);
            const dateStr = isNaN(date) ? "Invalid Date" : date.toLocaleString();

            return (
              <li key={i}>
                <button
                  onClick={() => handleFileClick(file.fileName)}
                  style={{
                    background: "none",
                    border: "none",
                    padding: 0,
                    color: "blue",
                    textDecoration: "underline",
                    cursor: "pointer",
                    fontSize: "inherit",
                    fontFamily: "inherit",
                  }}
                >
                  {file.fileName}
                </button>{" "}
                - {dateStr}
                <button
                  onClick={() => openDeleteModal(file.fileName)}
                  style={{ marginLeft: "10px", color: "red", cursor: "pointer" }}
                  title="Delete file"
                >
                  Delete
                </button>
              </li>
            );
          })}
        </ul>
      )}

      <ReactModal
        isOpen={modalOpen}
        onRequestClose={closeModal}
        contentLabel="Confirm Delete"
        style={{
          overlay: { backgroundColor: "rgba(0,0,0,0.5)" },
          content: { maxWidth: "400px", margin: "auto", padding: "20px" },
        }}
      >
        <h3>Confirm Delete</h3>
        <p>Are you sure you want to delete <strong>{fileToDelete}</strong>?</p>
        <button onClick={confirmDelete} style={{ marginRight: "10px" }}>
          Yes, Delete
        </button>
        <button onClick={closeModal}>Cancel</button>
      </ReactModal>

      <ToastContainer position="top-right" autoClose={3000} />
    </div>
  );
}
