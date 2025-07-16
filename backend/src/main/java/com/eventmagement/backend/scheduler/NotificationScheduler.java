package com.eventmagement.backend.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eventmagement.backend.model.Booking;
import com.eventmagement.backend.service.BookingService;
import com.eventmagement.backend.util.EmailUtil;

@Component
public class NotificationScheduler {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private EmailUtil emailUtil;

    @Scheduled(cron = "0 0 8 * * ?")
    public void sendReminderEmails() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Booking> bookings = bookingService.getAllBookingsForDate(tomorrow);

        for (Booking booking : bookings) {
            String to = booking.getUser().getEmail();
            String subject = "⏰ Reminder: Upcoming Event - " + booking.getEvent().getTitle();
            String content = String.format("""
                    <h3>Hello %s,</h3>
                    <p>This is a reminder that you have registered for the event <strong>%s</strong>.</p>
                    <p><strong>Date:</strong> %s<br/>
                    <strong>Location:</strong> %s</p>
                    <p>See you there!</p>
                    <hr/>
                    <p style="font-size: 0.9em; color: gray;">This is an automated reminder.</p>
                    """,
                    booking.getUser().getName(),
                    booking.getEvent().getTitle(),
                    booking.getEvent().getEventDate(),
                    booking.getEvent().getLocation());

            emailUtil.sendEmail(to, subject, content);
        }
    }
}
