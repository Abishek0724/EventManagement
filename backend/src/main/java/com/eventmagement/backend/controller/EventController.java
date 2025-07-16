package com.eventmagement.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventmagement.backend.dto.EventDTO;
import com.eventmagement.backend.model.Event;
import com.eventmagement.backend.model.User;
import com.eventmagement.backend.service.EventService;
import com.eventmagement.backend.service.UserService;
import com.eventmagement.backend.util.EmailUtil;

@RestController
@RequestMapping("/api/events")
@CrossOrigin("*")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailUtil emailUtil;

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventDTO eventDTO) {
        User organizer = userService.findByEmail(eventDTO.getOrganizerEmail());
        if (organizer == null) {
            return ResponseEntity.badRequest().body("Organizer not found with email: " + eventDTO.getOrganizerEmail());
        }

        Event event = new Event();
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setLocation(eventDTO.getLocation());
        event.setEventDate(eventDTO.getEventDate());
        event.setOrganizer(organizer);

        Event savedEvent = eventService.createEvent(event);

        String subject = "Event Confirmation: " + event.getTitle();
        String content = String.format("""
                    <h3>Hello %s,</h3>
                    <p>Your event <strong>%s</strong> has been successfully created.</p>
                    <p><strong>Date:</strong> %s<br/>
                    <strong>Location:</strong> %s</p>
                    <p>Thanks for using our platform!</p>
                    <hr/>
                    <p style="font-size: 0.9em; color: gray;">This is an automated email.</p>
                """, organizer.getName(), event.getTitle(), event.getEventDate(), event.getLocation());

        emailUtil.sendEmail(organizer.getEmail(), subject, content);

        return ResponseEntity.ok(eventDTO);
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> eventDTOs = eventService.getAllEvents().stream()
                .map(event -> {
                    EventDTO dto = new EventDTO();
                    dto.setId(event.getId());
                    dto.setTitle(event.getTitle());
                    dto.setDescription(event.getDescription());
                    dto.setLocation(event.getLocation());
                    dto.setEventDate(event.getEventDate());
                    dto.setOrganizerEmail(event.getOrganizer() != null ? event.getOrganizer().getEmail() : "Unknown");
                    return dto;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(eventDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody EventDTO dto) {
        Event updatedEvent = eventService.updateEvent(id, dto);
        if (updatedEvent == null) {
            return ResponseEntity.badRequest().body("Event not found");
        }
        return ResponseEntity.ok("Event updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        boolean deleted = eventService.deleteEvent(id);
        if (!deleted) {
            return ResponseEntity.badRequest().body("Event not found");
        }
        return ResponseEntity.ok("Event deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventDTO>> searchEvents(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String category) {

        List<Event> events = eventService.searchEvents(date, location, category);
        List<EventDTO> eventDTOs = events.stream().map(event -> {
            EventDTO dto = new EventDTO();
            dto.setId(event.getId());
            dto.setTitle(event.getTitle());
            dto.setDescription(event.getDescription());
            dto.setLocation(event.getLocation());
            dto.setEventDate(event.getEventDate());
            dto.setOrganizerEmail(event.getOrganizer() != null ? event.getOrganizer().getEmail() : "Unknown");
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(eventDTOs);
    }

}
