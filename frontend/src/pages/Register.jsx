import { useState } from "react";
import { register } from "../api/api";
import { useNavigate } from "react-router-dom";

const Register = () => {
  const [form, setForm] = useState({ name: "", email: "", password: "", role: "USER" });
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await register(form);
      alert("Registration successful");
      navigate("/login");
    } catch {
      alert("Registration failed");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="p-8 max-w-md mx-auto mt-10 shadow bg-white rounded">
      <h2 className="text-xl font-bold mb-4">Register</h2>
      <input placeholder="Name" className="input" onChange={(e) => setForm({ ...form, name: e.target.value })} />
      <input type="email" placeholder="Email" className="input mt-2" onChange={(e) => setForm({ ...form, email: e.target.value })} />
      <input type="password" placeholder="Password" className="input mt-2" onChange={(e) => setForm({ ...form, password: e.target.value })} />
      <select className="input mt-2" onChange={(e) => setForm({ ...form, role: e.target.value })}>
        <option value="USER">User</option>
        <option value="ADMIN">Admin</option>
      </select>
      <button className="btn mt-4 w-full">Register</button>
    </form>
  );
};

export default Register;
