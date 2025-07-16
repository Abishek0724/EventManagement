import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const Navbar = () => {
  const { user, logoutUser } = useAuth();

  return (
    <nav className="bg-gray-800 text-white p-4 flex justify-between items-center">
      <Link to="/" className="font-bold text-xl">EventManager</Link>

      <div className="space-x-4 flex items-center">
        {!user && (
          <>
            <Link to="/login" className="hover:underline">Login</Link>
            <Link to="/register" className="hover:underline">Register</Link>
          </>
        )}

        {user?.role === "ADMIN" && (
          <>
            <Link to="/admin" className="hover:underline">Admin Dashboard</Link>
            <Link to="/create-event" className="hover:underline">Create Event</Link>
          </>
        )}

        {user?.role === "USER" && (
          <>
            <Link to="/user" className="hover:underline">User Dashboard</Link>
            <Link to="/bookings" className="hover:underline">My Bookings</Link>
          </>
        )}

        {user && (
          <button
            onClick={logoutUser}
            className="bg-red-600 hover:bg-red-700 text-white px-3 py-1 rounded"
          >
            Logout
          </button>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
