import React, { useState } from "react";
import { shortenUrl } from "../api";

interface UrlShortenerProps {
  token?: string;
}

interface UrlResponse {
  shortUrl: string;
}

const UrlShortener: React.FC<UrlShortenerProps> = ({ token }) => {
  const [url, setUrl] = useState("");
  const [shortUrl, setShortUrl] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [customCode, setCustomCode] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setShortUrl("");
    setLoading(true);

    try {
    const data: UrlResponse = await shortenUrl(url, token || "", customCode);
    setShortUrl(data.shortUrl);
    } catch (err: any) {
      setError(err.response?.data?.message || "Failed to shorten URL.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card shadow-sm p-4" style={{ width: "100%", maxWidth: 500 }}>
      <h2 className="mb-4 text-center">🔗 URL Shortener</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          className="form-control mb-3"
          placeholder="Enter a URL..."
          value={url}
          onChange={(e) => setUrl(e.target.value)}
          required
        />
        <input
          type="text"
          className="form-control mb-3"
          placeholder="Optional: Custom ending (e.g., my-link)"
          value={customCode}
          onChange={(e) => setCustomCode(e.target.value)}
        />
        <button className="btn btn-primary w-100" disabled={loading}>
          {loading ? "Shortening..." : "Shorten"}
        </button>
      </form>

      {error && <div className="alert alert-danger mt-3">{error}</div>}
      {shortUrl && (
        <div className="alert alert-success mt-3">
          Shortened URL:{" "}
          <a href={shortUrl} target="_blank" rel="noopener noreferrer">
            {shortUrl}
          </a>
        </div>
      )}
    </div>
  );
};

export default UrlShortener;
