import { useEffect, useState } from "react";
import { getAllEvents, getBookingsByEvent, markAttendance } from "../api/api";

const AdminBookings = () => {
  const [events, setEvents] = useState([]);
  const [selectedEventId, setSelectedEventId] = useState("");
  const [bookings, setBookings] = useState([]);

  useEffect(() => {
    loadEvents();
  }, []);

  const loadEvents = async () => {
    try {
      const res = await getAllEvents();
      setEvents(res.data);
    } catch (err) {
      alert("Failed to load events");
    }
  };

  const fetchBookings = async (eventId) => {
    try {
      const res = await getBookingsByEvent(eventId);
      setBookings(res.data);
    } catch (err) {
      alert("Failed to load bookings");
    }
  };

  const handleAttendance = async (bookingId) => {
    try {
      await markAttendance(bookingId, true);
      alert("Marked as attended");
      fetchBookings(selectedEventId); 
    } catch (err) {
      alert("Failed to mark attendance");
    }
  };

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Manage Event Attendance</h2>

      <select
        className="border p-2 mb-4 rounded"
        onChange={(e) => {
          setSelectedEventId(e.target.value);
          fetchBookings(e.target.value);
        }}
        value={selectedEventId}
      >
        <option value="">Select Event</option>
        {events.map((e) => (
          <option key={e.id} value={e.id}>
            {e.title}
          </option>
        ))}
      </select>

      {bookings.length === 0 ? (
        <p>No bookings found.</p>
      ) : (
        <table className="w-full mt-4 border shadow text-sm">
          <thead className="bg-gray-100">
            <tr>
              <th className="p-2 border">User</th>
              <th className="p-2 border">Email</th>
              <th className="p-2 border">Attendance</th>
              <th className="p-2 border">Action</th>
            </tr>
          </thead>
          <tbody>
            {bookings.map((b) => (
              <tr key={b.id} className="text-center">
                <td className="p-2 border">{b.user?.name}</td>
                <td className="p-2 border">{b.user?.email}</td>
                <td className="p-2 border">
                  {b.attended ? "Attended" : "Not yet"}
                </td>
                <td className="p-2 border">
                  {!b.attended && (
                    <button
                      onClick={() => handleAttendance(b.id)}
                      className="bg-green-600 text-white px-2 py-1 rounded hover:bg-green-700"
                    >
                      Mark Attended
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default AdminBookings;
