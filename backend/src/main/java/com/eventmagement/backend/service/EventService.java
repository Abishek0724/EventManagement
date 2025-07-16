package com.eventmagement.backend.service;

import java.util.List;

import com.eventmagement.backend.dto.EventDTO;
import com.eventmagement.backend.model.Event;

public interface EventService {
    Event createEvent(Event event);

    List<Event> getAllEvents();

    boolean deleteEvent(Long id);

    Event updateEvent(Long id, EventDTO eventDTO);

    Event getEventById(Long id);

    List<Event> searchEvents(String date, String location, String category);

}
