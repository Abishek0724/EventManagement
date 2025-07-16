package com.eventmagement.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventmagement.backend.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
