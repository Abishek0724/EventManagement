import { useEffect, useState } from "react";
import { getMyBookings } from "../api/api";
import { useAuth } from "../context/AuthContext";

const Bookings = () => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();

  useEffect(() => {
    if (!user?.email) return;

    const fetchBookings = async () => {
      try {
        const res = await getMyBookings(user.email);
        setBookings(res.data);
      } catch (err) {
        console.error("Failed to fetch bookings:", err);
        setBookings([]);
      } finally {
        setLoading(false);
      }
    };

    fetchBookings();
  }, [user]);

  if (loading) return <div className="p-6">Loading bookings...</div>;

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">My Bookings</h2>
      {bookings.length === 0 ? (
        <p>No bookings found.</p>
      ) : (
        bookings.map((b, i) => (
          <div key={i} className="p-4 border mb-2 rounded bg-white shadow">
            <h3 className="text-lg font-semibold">{b.event?.title}</h3>
            <p><strong>Location:</strong> {b.event?.location}</p>
            <p><strong>Date:</strong> {b.event?.eventDate}</p>
          </div>
        ))
      )}
    </div>
  );
};

export default Bookings;
