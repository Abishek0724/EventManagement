package com.eventmagement.backend;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;

import com.eventmagement.backend.model.Booking;
import com.eventmagement.backend.model.Event;
import com.eventmagement.backend.model.User;
import com.eventmagement.backend.repository.BookingRepository;
import com.eventmagement.backend.repository.EventRepository;
import com.eventmagement.backend.repository.UserRepository;
import com.eventmagement.backend.service.impl.BookingServiceImpl;
import com.eventmagement.backend.service.impl.EventServiceImpl;
import com.eventmagement.backend.service.impl.UserServiceImpl;

@SpringBootTest
class BackendApplicationTests {

	@Mock
	private UserRepository userRepository;

	@Mock
	private EventRepository eventRepository;

	@Mock
	private BookingRepository bookingRepository;

	@InjectMocks
	private UserServiceImpl userService;

	@InjectMocks
	private EventServiceImpl eventService;

	@InjectMocks
	private BookingServiceImpl bookingService;

	private User mockUser;
	private Event mockEvent;

	@BeforeEach
	void setup() {
		mockUser = new User(1L, "Abishek", "abhi@example.com", "password123", "USER");
		mockEvent = new Event(1L, "Tech Conference", "Tech Desc", "Chennai", LocalDateTime.now(), mockUser, "Tech");
	}

	@Test
	void testSaveUser() {
		when(userRepository.save(any(User.class))).thenReturn(mockUser);
		User saved = userRepository.save(mockUser);
		assertEquals("Abishek", saved.getName());
		assertEquals("USER", saved.getRole());
	}

	@Test
	void testSaveEvent() {
		when(eventRepository.save(any(Event.class))).thenReturn(mockEvent);
		Event saved = eventService.createEvent(mockEvent);
		assertEquals("Tech Conference", saved.getTitle());
		assertEquals("Tech", saved.getCategory());
	}

	@Test
	void testBookEvent() {
		Booking mockBooking = new Booking(1L, mockUser, mockEvent, false);

		when(bookingRepository.save(any(Booking.class))).thenReturn(mockBooking);
		Booking booked = bookingService.bookEvent(mockUser, mockEvent); // fix method signature

		assertEquals(mockUser.getEmail(), booked.getUser().getEmail());
		assertFalse(booked.isAttended());
	}
}
