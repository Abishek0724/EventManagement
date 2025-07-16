import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

API.interceptors.request.use((req) => {
  const token = localStorage.getItem("token");

  const isPublicEndpoint = req.url.includes("/events/search");

  if (token && !isPublicEndpoint) {
    req.headers.Authorization = `Bearer ${token}`;
  }

  return req;
});

export const login = (data) => API.post("/auth/login", data);
export const register = (data) => API.post("/users/register", data);

export const getAllEvents = () => API.get("/events");
export const getEventById = (id) => API.get(`/events/${id}`);
export const createEvent = (data) => API.post("/events", data);  
export const updateEvent = (id, data) => API.put(`/events/${id}`, data);
export const deleteEvent = (id) => API.delete(`/events/${id}`);


export const searchEvents = (params) => API.get("/events/search", { params });  

export const bookEvent = (email, eventId) =>
  API.post(`/bookings/book?email=${email}&eventId=${eventId}`);

export const getMyBookings = () => API.get("/bookings/my"); 

export const getBookingsByEvent = (eventId) =>
  API.get(`/bookings/attendees?eventId=${eventId}`);

export const markAttendance = (bookingId, attended) =>
  API.put(`/bookings/attendance?bookingId=${bookingId}&attended=${attended}`);

