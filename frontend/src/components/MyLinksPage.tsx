import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { properties } from "../config/properties";
import { getUserLinks } from "../api";

interface ShortUrl {
  code: string;
  longUrl: string;
  createdAt: string;
  clickCount: number;
}

const MyLinksPage: React.FC = () => {
  const [links, setLinks] = useState<ShortUrl[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchLinks = async () => {
      try {
        const data: ShortUrl[] = await getUserLinks();
        setLinks(data);
      } catch (err: any) {
        setError(err.response?.data?.message || "Failed to fetch links.");
      } finally {
        setLoading(false);
      }
    };

    fetchLinks();
  }, []);

  if (loading) return <p>Loading...</p>;
  if (error) return <div className="alert alert-danger">{error}</div>;

  return (
    <div className="container mt-4">
      <h2>My Shortened Links</h2>
         <div className="mb-3">
            <Link to="/" className="btn btn-secondary">← Back to Home</Link>
         </div>
      {links.length === 0 ? (
        <p>You have not created any links yet.</p>
      ) : (
        <table className="table table-striped mt-3">
          <thead>
            <tr>
              <th>Short URL</th>
              <th>Original URL</th>
              <th>Created At</th>
              <th>Clicks</th>
            </tr>
          </thead>
          <tbody>
            {links.map((link) => (
              <tr key={link.code}>
                <td>
                  <a href={`http://localhost:${properties.serverPort}/${link.code}`} target="_blank" rel="noopener noreferrer">
                    {`http://localhost:${properties.serverPort}`}/{link.code}
                  </a>
                </td>
                <td>
                  <a href={link.longUrl} target="_blank" rel="noopener noreferrer">
                    {link.longUrl}
                  </a>
                </td>
                <td>{new Date(link.createdAt).toLocaleString()}</td>
                <td>{link.clickCount}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default MyLinksPage;
