package com.eventmagement.backend.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventmagement.backend.dto.EventDTO;
import com.eventmagement.backend.model.Event;
import com.eventmagement.backend.model.User;
import com.eventmagement.backend.repository.EventRepository;
import com.eventmagement.backend.service.EventService;
import com.eventmagement.backend.service.UserService;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserService userService;

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public boolean deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            return false;
        }
        eventRepository.deleteById(id);
        return true;
    }

    @Override
    public Event updateEvent(Long id, EventDTO eventDTO) {
        Event existingEvent = eventRepository.findById(id).orElse(null);
        if (existingEvent == null)
            return null;

        existingEvent.setTitle(eventDTO.getTitle());
        existingEvent.setDescription(eventDTO.getDescription());
        existingEvent.setLocation(eventDTO.getLocation());
        existingEvent.setEventDate(eventDTO.getEventDate());
        existingEvent.setCategory(eventDTO.getCategory());

        if (eventDTO.getOrganizerEmail() != null) {
            User organizer = userService.findByEmail(eventDTO.getOrganizerEmail());
            if (organizer != null) {
                existingEvent.setOrganizer(organizer);
            }
        }

        return eventRepository.save(existingEvent);
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public List<Event> searchEvents(String date, String location, String category) {
        List<Event> allEvents = eventRepository.findAll();

        return allEvents.stream()
                .filter(event -> {
                    boolean matches = true;

                    if (date != null && !date.isEmpty()) {
                        LocalDate inputDate = LocalDate.parse(date);
                        matches &= event.getEventDate().equals(inputDate);
                    }

                    if (location != null && !location.isEmpty()) {
                        matches &= event.getLocation().toLowerCase().contains(location.toLowerCase());
                    }

                    if (category != null && !category.isEmpty()) {
                        matches &= event.getDescription().toLowerCase().contains(category.toLowerCase());
                    }

                    return matches;
                })
                .collect(Collectors.toList());
    }

}
