import React, { useState } from 'react';
import './MovieXmlImport.css';
import axios from 'axios';
function MovieXmlImport() {
  const [file, setFile] = useState(null);
  const [dragActive, setDragActive] = useState(false);
  const [message, setMessage] = useState('');
  const [status, setStatus] = useState(null);
  const [isUploading, setIsUploading] = useState(false);
  const [importType, setImportType] = useState('MOVIES');

  const handleFile = (f) => {
    if (f && f.type === 'text/xml') {
      setFile(f);
      setMessage('');
      setStatus(null);
    } else {
      setMessage('Only .xml files are allowed!');
      setStatus('error');
    }
  };

  const handleDrag = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setDragActive(e.type === 'dragenter' || e.type === 'dragover');
  };

  const handleDrop = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setDragActive(false);
    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      handleFile(e.dataTransfer.files[0]);
    }
    console.log(e.dataTransfer.files[0]);
  };

  const handleFileChange = (e) => {
    handleFile(e.target.files[0]);
  };

  const handleUpload = async () => {
    if (!file) {
      setStatus('error');
      setMessage('Please select a file first.');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);
    formData.append('type', importType);

    try {
      setIsUploading(true);
      setMessage('');
      const res = await axios.post('http://localhost:8080/xml/import', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      setStatus('success');
      setMessage('Import successful!');
    } catch (err) {
      setStatus('error');
      setMessage(err.response?.data?.details || 'Import failed.');
      console.log(err);
    } finally {
      setIsUploading(false);
    }
  };

  return (
    <div className="upload-page-wrapper">
      <div className="xml-upload-section">
        <h2>Import XML File</h2>

        <label htmlFor="import-type">Select Type:</label>
        <select value={importType} onChange={(e) => setImportType(e.target.value)}>
          <option value="MOVIES">Movies</option>
          <option value="SHOWTIMES">Showtimes</option>
        </select>

        {/* Drag & Drop Zone */}
        <div
          className={`drop-zone ${dragActive ? 'active' : ''}`}
          onDragEnter={handleDrag}
          onDragOver={handleDrag}
          onDragLeave={handleDrag}
          onDrop={handleDrop}
        >
          <p>Drag & Drop XML file here or</p>
          <label htmlFor="file-upload" className="custom-file-label">Browse File</label>
          <input
            id="file-upload"
            type="file"
            accept=".xml"
            onChange={handleFileChange}
            className="custom-file-input"
          />
          {file && <span className="file-name"> {file.name}</span>}
        </div>

        <button onClick={handleUpload} disabled={isUploading}>
          {isUploading ? (
            <span className="uploading-text">
              <span className="spinner" /> Uploading...
            </span>
          ) : (
            `Upload ${importType} XML`
          )}
        </button>

        {message && (
          <p className={`status-message ${status} ${status === 'error' ? 'shake' : ''}`}>
            {message}
          </p>
        )}
      </div>
    </div>
  );
}

export default MovieXmlImport;
