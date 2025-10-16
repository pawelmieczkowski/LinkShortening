import React, {useState, useEffect} from "react";
import { Link } from "react-router-dom";
import UrlShortener from "./UrlShortener";

const HomePage: React.FC = () => {
  const [token, setToken] = useState<string | null>(sessionStorage.getItem("accessToken"));

  useEffect(() => {
    const handleStorageChange = () => {
      setToken(sessionStorage.getItem("accessToken"));
    };

    window.addEventListener("storage", handleStorageChange);
    return () => window.removeEventListener("storage", handleStorageChange);
  }, []);

  const handleLogout = () => {
    sessionStorage.removeItem("accessToken");
    setToken(null);
  };

  return (
    <div className="d-flex flex-column align-items-center" style={{ minHeight: "100vh", paddingTop: 50 }}>
      <UrlShortener token={token || ""} />

      {!token ? (
  <div className="mt-3 d-flex gap-2">
    <Link to="/login" className="btn btn-primary">Login</Link>
    <Link to="/register" className="btn btn-success">Register</Link>
  </div>
) : (
  <div className="mt-3 d-flex gap-2">
    <Link to="/my-links" className="btn btn-info">My Links</Link>
    <button className="btn btn-danger" onClick={handleLogout}>Logout</button>
  </div>
)}

    </div>
  );
};

export default HomePage;
