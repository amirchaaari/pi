package com.example.pi.interfaces.trainingSession;

import com.example.pi.entity.Booking;

import java.util.List;

public interface IBookingService {
    public Booking createBooking(Long sessionId);
    public String cancelBooking(Long bookingId);
    public void processCoachDecision(Long bookingId, Booking.Status decision);
    public List<Booking> getBookingsByUserId();
}
