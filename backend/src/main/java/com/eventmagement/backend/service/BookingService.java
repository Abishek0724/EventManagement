package com.eventmagement.backend.service;

import java.time.LocalDate;
import java.util.List;

import com.eventmagement.backend.model.Booking;
import com.eventmagement.backend.model.Event;
import com.eventmagement.backend.model.User;

public interface BookingService {
    Booking bookEvent(User user, Event event);

    List<Booking> getBookingsByUser(User user);

    Booking markAttendance(Long bookingId, boolean status);

    List<Booking> getAllBookingsForDate(LocalDate date);

    List<Booking> getBookingsByEvent(Event event);

}
