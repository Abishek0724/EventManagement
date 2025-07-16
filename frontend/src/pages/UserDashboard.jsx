import { useEffect, useState } from "react";
import { searchEvents, bookEvent } from "../api/api";
import { useAuth } from "../context/AuthContext";

const UserDashboard = () => {
  const { user } = useAuth();
  const [events, setEvents] = useState([]);
  const [filters, setFilters] = useState({
    date: "",
    location: "",
    category: "",
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchFilteredEvents(); 
  }, []);

  const fetchFilteredEvents = async () => {
    setLoading(true);
    try {
      const res = await searchEvents(filters);
      setEvents(res.data);
    } catch (err) {
      console.error("Error loading events", err);
      alert("Failed to load events");
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    setFilters({ ...filters, [e.target.name]: e.target.value });
  };

  const handleBook = async (eventId) => {
    if (!user || !user.email) {
      alert("Please log in to book.");
      return;
    }

    try {
      await bookEvent(user.email, eventId);
      alert("Booking successful!");
    } catch (err) {
      console.error("Booking failed", err);
      alert("Booking failed. You may have already booked this event.");
    }
  };

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Search Events</h2>

      {/* Filter Form */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
        <input
          type="date"
          name="date"
          value={filters.date}
          onChange={handleInputChange}
          className="border p-2 rounded"
        />
        <input
          type="text"
          name="location"
          placeholder="Location"
          value={filters.location}
          onChange={handleInputChange}
          className="border p-2 rounded"
        />
        <input
          type="text"
          name="category"
          placeholder="Category"
          value={filters.category}
          onChange={handleInputChange}
          className="border p-2 rounded"
        />
        <button
          onClick={fetchFilteredEvents}
          className="sm:col-span-3 bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
        >
          Search
        </button>
      </div>

      {/* Events */}
      <h2 className="text-xl font-semibold mb-2">Available Events</h2>
      {loading ? (
        <p>Loading...</p>
      ) : events.length === 0 ? (
        <p>No events found.</p>
      ) : (
        events.map((e) => (
          <div key={e.id} className="p-4 border mt-4 rounded shadow bg-white">
            <h3 className="text-xl font-semibold">{e.title}</h3>
            <p className="text-gray-700">{e.description}</p>
            <p className="text-sm text-gray-500 mt-1">
              📍 {e.location} | 📅 {e.eventDate} | 🏷️ {e.category}
            </p>
            <button
              onClick={() => handleBook(e.id)}
              className="mt-3 bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
            >
              Book
            </button>
          </div>
        ))
      )}
    </div>
  );
};

export default UserDashboard;
