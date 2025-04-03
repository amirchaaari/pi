package com.example.pi.controller.TrainingSessionControllers;

import com.example.pi.entity.Booking;
import com.example.pi.entity.TrainingSession;
import com.example.pi.service.trainingSessionServices.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@AllArgsConstructor
public class BookingController {

    @Autowired
    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Booking createBooking(@RequestParam Long sessionId) {
        return bookingService.createBooking(sessionId);
    }

    @GetMapping("/retrieve-Bookings")
    public List<Booking> getTrainingSessions() {
        return bookingService.getBookingsByUserId();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ROLE_COACH')")
    public void approveBooking(@PathVariable Long id) {
        bookingService.processCoachDecision(id, Booking.Status.APPROVED);
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('ROLE_COACH')")
    public void rejectBooking(@PathVariable Long id) {
        bookingService.processCoachDecision(id, Booking.Status.REJECTED);
    }
}
