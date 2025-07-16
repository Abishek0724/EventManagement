import { useState } from "react";
import { createEvent } from "../api/api";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const CreateEvent = () => {
  const { user } = useAuth();
  const [eventData, setEventData] = useState({
    title: "",
    description: "",
    eventDate: "",
    location: "",
  });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setEventData({ ...eventData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const { title, description, eventDate, location } = eventData;
    if (!title || !description || !eventDate || !location) {
      alert("All fields are required.");
      return;
    }

    try {
      setLoading(true);
      const payload = {
        ...eventData,
        organizerEmail: user?.email,
      };
      await createEvent(payload);
      alert("Event created successfully!");
      navigate("/admin");
    } catch (err) {
      console.error(err);
      alert("Failed to create event.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-lg mx-auto mt-10 p-6 bg-white rounded shadow">
      <h2 className="text-2xl font-semibold mb-6">Create New Event</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          type="text"
          name="title"
          placeholder="Title"
          className="input w-full border p-2"
          value={eventData.title}
          onChange={handleChange}
        />
        <textarea
          name="description"
          placeholder="Description"
          className="input w-full border p-2"
          value={eventData.description}
          onChange={handleChange}
        />
        <input
          type="datetime-local"
          name="eventDate"
          className="input w-full border p-2"
          value={eventData.eventDate}
          onChange={handleChange}
        />
        <input
          type="text"
          name="location"
          placeholder="Location"
          className="input w-full border p-2"
          value={eventData.location}
          onChange={handleChange}
        />
        <button
          type="submit"
          className="btn bg-blue-600 text-white px-4 py-2 w-full rounded"
          disabled={loading}
        >
          {loading ? "Creating..." : "Create Event"}
        </button>
      </form>
    </div>
  );
};

export default CreateEvent;
