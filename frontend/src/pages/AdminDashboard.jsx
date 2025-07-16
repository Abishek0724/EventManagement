import { useEffect, useState } from "react";
import { getAllEvents, deleteEvent } from "../api/api";
import { Link } from "react-router-dom";
const AdminDashboard = () => {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchEvents();
  }, []);

  const fetchEvents = async () => {
    try {
      const res = await getAllEvents();
      setEvents(res.data);
    } catch (err) {
      console.error("Error fetching events:", err);
      setError("Failed to load events.");
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteEvent(id);
      setEvents(events.filter((e) => e.id !== id));
      alert("Event deleted successfully.");
    } catch (err) {
      console.error("Delete failed:", err);
      alert("Failed to delete event.");
    }
  };

  if (loading) return <div className="p-6">Loading events...</div>;
  if (error) return <div className="p-6 text-red-500">{error}</div>;

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Admin Dashboard</h2>
      {events.length === 0 ? (
        <p>No events found.</p>
      ) : (
        <ul>
          {events.map((e) => (
            <li
              key={e.id}
              className="mt-2 flex justify-between items-center border p-3 rounded bg-white shadow"
            >
              <div>
                <p className="font-semibold">{e.title}</p>
                <p className="text-sm text-gray-600">
                  {e.eventDate} • {e.location}
                </p>
              </div>
              <button
                onClick={() => handleDelete(e.id)}
                className="text-red-600 hover:text-red-800"
              >
                Delete
              </button>
            </li>
          ))}
        </ul>
      )}
        <div className="flex justify-center mt-10">
      <Link to="/admin/bookings" className="inline-flex items-center gap-2 px-5 py-2 bg-blue-100 text-blue-700 hover:bg-blue-200 transition-colors duration-300 rounded-full shadow-sm hover:shadow-md font-medium">
        <span className="text-xl">➕</span>
        <span>Manage Attendance</span>
      </Link>
    </div>


    </div>
  );
};

export default AdminDashboard;
