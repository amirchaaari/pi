package com.example.pi.interfaces.trainingSession;

import com.example.pi.entity.Booking;
import com.example.pi.entity.UserInfo;

import java.util.List;

public interface IBookingService {
    Booking bookSession(Booking booking, UserInfo user, Long sessionId);
    List<Booking> getBookingsBySession(Long sessionId);
    void cancelBooking(Long bookingId);
}
