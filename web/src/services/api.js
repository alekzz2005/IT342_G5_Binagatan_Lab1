import axios from "axios";

const API_BASE_URL =
	process.env.REACT_APP_API_URL || "http://localhost:8080";

const api = axios.create({
	baseURL: API_BASE_URL,
	headers: {
		"Content-Type": "application/json",
	},
});

api.interceptors.request.use((config) => {
	const token = localStorage.getItem("auth_token");
	if (token) {
		config.headers.Authorization = `Bearer ${token}`;
	}
	return config;
});

export const setAuthToken = (token) => {
	if (token) {
		localStorage.setItem("auth_token", token);
	}
};

export const clearAuthToken = () => {
	localStorage.removeItem("auth_token");
};

export const getAuthToken = () => localStorage.getItem("auth_token");

export const registerUser = async (payload) => {
	const response = await api.post("/api/auth/register", payload);
	return response.data;
};

export const loginUser = async (payload) => {
	const response = await api.post("/api/auth/login", payload);
	return response.data;
};

export const fetchMe = async () => {
	const response = await api.get("/api/user/me");
	return response.data;
};

export const logoutUser = async () => {
	await api.post("/api/auth/logout");
};

export default api;
