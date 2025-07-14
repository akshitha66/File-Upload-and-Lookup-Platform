import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import FileUpload from "./components/FileUpload";
import UploadedFilesList from "./components/UploadedFilesList";
import "./App.css";

function Home() {
  return (
    <div className="upload-container">
      <FileUpload />
      <Link to="/uploaded-files">
        <button className="view-files-button">Check Uploaded Files</button>
      </Link>
    </div>
  );
}

function App() {
  return (
    <Router>
      <div className="app-page">
        <h1 className="app-title">Filer Uploader</h1>

        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/uploaded-files" element={<UploadedFilesList />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
