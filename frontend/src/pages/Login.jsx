import { useState } from "react";
import { login } from "../api/api";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

const Login = () => {
  const [form, setForm] = useState({ email: "", password: "" });
  const { loginUser } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await login(form);
      loginUser(res.data);
      navigate(res.data.role === "ADMIN" ? "/admin" : "/user");
    } catch {
      alert("Login failed");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="p-8 max-w-md mx-auto mt-10 shadow bg-white rounded">
      <h2 className="text-xl font-bold mb-4">Login</h2>
      <input type="email" placeholder="Email" className="input" onChange={(e) => setForm({ ...form, email: e.target.value })} />
      <input type="password" placeholder="Password" className="input mt-2" onChange={(e) => setForm({ ...form, password: e.target.value })} />
      <button className="btn mt-4 w-full">Login</button>
    </form>
  );
};

export default Login;
