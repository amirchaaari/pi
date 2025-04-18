package com.example.pi.service.trainingSessionServices;

import com.example.pi.entity.Booking;
import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;
import com.example.pi.interfaces.trainingSession.IBookingService;

import com.example.pi.repository.UserInfoRepository;
import com.example.pi.repository.trainignSessionRepo.BookingRepository;
import com.example.pi.repository.trainignSessionRepo.TrainingSessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {

    @Autowired
    private final BookingRepository bookingRepository;
    @Autowired
    private final UserInfoRepository userInfoRepository;
    @Autowired
    private final TrainingSessionRepository trainingSessionRepository;
    @Autowired
    private final NotificationService notificationService;


    private UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userInfoRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public Booking createBooking(Long sessionId) {
        UserInfo user = getCurrentUser();
        TrainingSession session = trainingSessionRepository.findById(sessionId)
                .filter(s -> s.getEndTime().isAfter(LocalTime.now()))
                .orElseThrow(() -> new RuntimeException("Invalid session"));

        if (bookingRepository.existsByUserAndTrainingSession(user, session)) {
            throw new IllegalStateException("Already booked");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTrainingSession(session);
        return bookingRepository.save(booking);
    }

    @Transactional
    public String cancelBooking(Long bookingId) {
        UserInfo user = getCurrentUser();
        Booking booking = bookingRepository.findById(bookingId)
                .filter(b -> b.getUser().equals(user))
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        bookingRepository.delete(booking);
        return "Booking cancelled";
    }

    @Transactional
    public void processCoachDecision(Long bookingId, Booking.Status decision) {
        UserInfo coach = getCurrentUser();
        Booking booking = bookingRepository.findById(bookingId)
                .filter(b -> b.getTrainingSession().getCoach().equals(coach))
                .orElseThrow(() -> new RuntimeException("Unauthorized action"));

        booking.setStatus(decision);
        booking.setResolvedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        if (decision == Booking.Status.APPROVED) {
            notificationService.notifyUser(booking.getUser(),
                    "Booking approved for: " + booking.getTrainingSession().getDescription());
        }
    }

    @Override
    public List<Booking> getBookingsByUserId() {
        UserInfo user = getCurrentUser();
        return bookingRepository.findByUser(user);
    }

    @Scheduled(cron = "0 * * * * *") // Every minute for demo
    public void autoRejectExpiredBookings() {
        LocalDateTime now = LocalDateTime.now();
        bookingRepository.findByStatusAndTrainingSessionEndTimeBefore(Booking.Status.PENDING, now.toLocalTime())
                .forEach(booking -> {
                    booking.setStatus(Booking.Status.REJECTED);
                    booking.setResolvedAt(now);
                    bookingRepository.save(booking);
                    notificationService.notifyUser(booking.getUser(),
                            "Booking auto-rejected for: " + booking.getTrainingSession().getDescription());
                });
    }
}
