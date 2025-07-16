package com.eventmagement.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventmagement.backend.model.Booking;
import com.eventmagement.backend.model.Event;
import com.eventmagement.backend.model.User;
import com.eventmagement.backend.service.BookingService;
import com.eventmagement.backend.service.EventService;
import com.eventmagement.backend.service.UserService;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin("*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @PostMapping("/book")
    public ResponseEntity<?> bookEvent(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long eventId) {
        String email = userDetails.getUsername();
        User user = userService.findByEmail(email);
        Event event = eventService.getEventById(eventId);

        if (user == null || event == null) {
            return ResponseEntity.badRequest().body("Invalid user or event.");
        }

        Booking booking = bookingService.bookEvent(user, event);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Booking>> getMyBookings(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userService.findByEmail(email);

        if (user == null)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(bookingService.getBookingsByUser(user));
    }

    @PutMapping("/attendance")
    public ResponseEntity<?> updateAttendance(@RequestParam Long bookingId, @RequestParam boolean attended) {
        Booking updated = bookingService.markAttendance(bookingId, attended);
        if (updated == null) {
            return ResponseEntity.badRequest().body("Booking not found");
        }
        return ResponseEntity.ok("Attendance updated");
    }

    @GetMapping("/attendees")
    public ResponseEntity<?> getAttendees(@RequestParam Long eventId) {
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            return ResponseEntity.badRequest().body("Event not found");
        }

        List<Booking> bookings = bookingService.getBookingsByEvent(event);
        return ResponseEntity.ok(bookings);
    }

}
