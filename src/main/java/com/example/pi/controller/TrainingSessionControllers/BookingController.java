package com.example.pi.controller.TrainingSessionControllers;

import com.example.pi.entity.Booking;
import com.example.pi.service.trainingSessionServices.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/training-sessions")
@AllArgsConstructor
public class BookingController {

    @Autowired
    private final BookingService bookingService;

    @PostMapping("/{sessionId}/bookings")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Booking createBooking(@PathVariable Long sessionId) {
        return bookingService.createBooking(sessionId);
    }

    @GetMapping("/{sessionId}/bookings/retrieve-Bookings")
    public List<Booking> getBookings() {
        return bookingService.getBookingsByUserId();
    }

    @DeleteMapping("/{sessionId}/bookings/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
    }

    @GetMapping("/getCoachBookings")
    @PreAuthorize("hasRole('ROLE_COACH')")
    public List<Booking> getBookingsByCoachId() {
        return bookingService.getBookingsByCoach();
    }

    @PatchMapping("/{sessionId}/bookings/{id}/approve")
    @PreAuthorize("hasRole('ROLE_COACH')")
    public void approveBooking(@PathVariable Long id) {
        bookingService.processCoachDecision(id, Booking.Status.APPROVED);
    }

    @PatchMapping("/{sessionId}/bookings/{id}/reject")
    @PreAuthorize("hasRole('ROLE_COACH')")
    public void rejectBooking(@PathVariable Long id) {
        bookingService.processCoachDecision(id, Booking.Status.REJECTED);
    }
}
