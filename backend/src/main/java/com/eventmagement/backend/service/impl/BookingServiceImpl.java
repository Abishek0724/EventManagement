package com.eventmagement.backend.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventmagement.backend.model.Booking;
import com.eventmagement.backend.model.Event;
import com.eventmagement.backend.model.User;
import com.eventmagement.backend.repository.BookingRepository;
import com.eventmagement.backend.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public Booking bookEvent(User user, Event event) {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setEvent(event);
        booking.setAttended(false);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    @Override
    public Booking markAttendance(Long bookingId, boolean status) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null) {
            booking.setAttended(status);
            return bookingRepository.save(booking);
        }
        return null;
    }

    @Override
    public List<Booking> getAllBookingsForDate(LocalDate date) {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getEvent().getEventDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> getBookingsByEvent(Event event) {
        return bookingRepository.findByEvent(event);
    }

}
