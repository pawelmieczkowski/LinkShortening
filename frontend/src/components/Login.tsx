import React, { useState } from "react";
import { login } from "../api";
import { Link, useNavigate } from "react-router-dom";

const Login: React.FC = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      await login(username, password);
      navigate("/");
    } catch (err: any) {
      setError(err.response?.data?.message || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="d-flex justify-content-center align-items-center" style={{ minHeight: "100vh", background: "#f0f2f5" }}>
      <div className="card p-4 shadow-sm" style={{ maxWidth: 400, width: "100%" }}>
        <h3 className="mb-3 text-center">Login</h3>
        <form onSubmit={handleSubmit}>
          <input type="text" className="form-control mb-2" placeholder="Username" value={username} onChange={e => setUsername(e.target.value)} required />
          <input type="password" className="form-control mb-2" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} required />
          <button className="btn btn-primary w-100" disabled={loading}>{loading ? "Logging in..." : "Login"}</button>
        </form>
        {error && <div className="alert alert-danger mt-2">{error}</div>}
        <div className="d-flex justify-content-between mt-3">
          <Link to="/" className="btn btn-outline-secondary">Back to Main Page</Link>
          <Link to="/register" className="btn btn-success">Register</Link>
        </div>
      </div>
    </div>
  );
};

export default Login;
