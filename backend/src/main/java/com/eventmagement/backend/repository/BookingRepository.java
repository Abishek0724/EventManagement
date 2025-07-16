package com.eventmagement.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eventmagement.backend.model.Booking;
import com.eventmagement.backend.model.Event;
import com.eventmagement.backend.model.User;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUser(User user);

    List<Booking> findByEvent(Event event);

    @Query("SELECT b FROM Booking b WHERE b.event.eventDate = :date")
    List<Booking> findByEventDate(LocalDate date);
}
