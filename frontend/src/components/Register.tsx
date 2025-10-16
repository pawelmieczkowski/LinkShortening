import React, { useState } from "react";
import { register } from "../api";
import { Link, useNavigate } from "react-router-dom";

const Register: React.FC = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      await register(username, email, password);
      navigate("/");
    } catch (err: any) {
      setError(err.response?.data?.message || "Registration failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="d-flex justify-content-center align-items-center" style={{ minHeight: "100vh", background: "#f0f2f5" }}>
      <div className="card p-4 shadow-sm" style={{ maxWidth: 400, width: "100%" }}>
        <h3 className="mb-3 text-center">Register</h3>
        <form onSubmit={handleSubmit}>
          <input type="text" className="form-control mb-2" placeholder="Username" value={username} onChange={e => setUsername(e.target.value)} required />
          <input type="email" className="form-control mb-2" placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} required />
          <input type="password" className="form-control mb-2" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} required />
          <button className="btn btn-success w-100" disabled={loading}>{loading ? "Registering..." : "Register"}</button>
        </form>
        {error && <div className="text-danger mt-2">{error}</div>}
        <div className="d-flex justify-content-between mt-3">
          <Link to="/" className="btn btn-outline-secondary">Back to Main Page</Link>
          <Link to="/login" className="btn btn-primary">Login</Link>
        </div>
      </div>
    </div>
  );
};

export default Register;
