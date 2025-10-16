import axios from "axios";
import { properties } from "./config/properties";

const backendBase = `http://localhost:${properties.serverPort}`;


export const api = axios.create({
  baseURL: backendBase,
});

api.interceptors.request.use((config) => {
  const token = sessionStorage.getItem("accessToken");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const login = async (username: string, password: string) => {
  const response = await api.post(
    "/login",
    { username, password },
    {
      headers: { "Content-Type": "application/json" },
    }
  );
  const authHeader = response.headers["authorization"] || response.headers["Authorization"];
  if (!authHeader || !authHeader.startsWith("Bearer ")) {
    throw new Error("Token missing in header");
  }
  const token = authHeader.substring(7);
  sessionStorage.setItem("accessToken", token);
  return response.data;
};

export const register = async (username: string, email: string, password: string) => {
  const response = await api.post("/registration", { username, email, password });
  return response.data;
};

export const shortenUrl = async (url: string, token: string, customCode?: string) => {
  const body: any = { url };
      console.log(customCode)
  if (customCode && customCode.trim() !== "") {
    body.customCode = customCode.trim();
  }

  const response = await api.post(
    "/shorten",
    { url, customCode },
    { headers: { Authorization: `Bearer ${token}` } }
  );
  return response.data;
};

export const getUserLinks = async () => {
  const response = await api.get("/private/my-links");
  return response.data;
};
