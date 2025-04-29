package com.example.pi.service.trainingSessionServices;

import com.example.pi.entity.Booking;
import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;
import com.example.pi.interfaces.trainingSession.IBookingService;
import com.example.pi.repository.UserInfoRepository;
import com.example.pi.repository.trainignSessionRepo.BookingRepository;
import com.example.pi.repository.trainignSessionRepo.TrainingSessionRepository;
import com.example.pi.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final EmailService emailService;
    @Autowired
    private final UserScoringService userScoringService;

    private UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userInfoRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public Booking createBooking(Long sessionId) {
        UserInfo user = getCurrentUser();
        Optional<TrainingSession> session = trainingSessionRepository.findById(sessionId);
//                .filter(s -> s.getEndTime().isAfter(LocalTime.now()))
//                .orElseThrow(() -> new RuntimeException("Invalid session"));
        TrainingSession trainingSession = session.orElseThrow(() -> new RuntimeException("Invalid session"));
        if (bookingRepository.existsByUserAndTrainingSession(user, trainingSession)) {
            throw new IllegalStateException("Already booked");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTrainingSession(trainingSession);
        userScoringService.classifyUsers(user);
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
            //Send the booking confirmation email
            emailService.sendBookingConfirmationEmail(booking.getUser().getEmail(),
                    booking.getTrainingSession().getDescription(), booking.getTrainingSession().getDate(),booking.getTrainingSession().getStartTime());
            System.out.println("mail sent to " + booking.getUser().getEmail());
        }else if (decision == Booking.Status.REJECTED) {
            emailService.sendBookingRejectionEmail(booking.getUser().getEmail(),booking.getTrainingSession().getDescription());
            System.out.println("mail sent to " + booking.getUser().getEmail());

        }

    }

    @Override
    public List<Booking> getBookingsByUserId() {
        UserInfo user = getCurrentUser();
        return bookingRepository.findByUser(user);
    }

    @Scheduled(cron = "0 * * * * *")
    public void autoRejectExpiredBookings() {
        LocalDateTime now = LocalDateTime.now();
        bookingRepository.findByStatusAndTrainingSessionEndTimeBefore(Booking.Status.PENDING, now.toLocalTime())
                .forEach(booking -> {
                    booking.setStatus(Booking.Status.REJECTED);
                    booking.setResolvedAt(now);
                    bookingRepository.save(booking);
                    emailService.sendBookingRejectionEmail(booking.getUser().getEmail(),booking.getTrainingSession().getDescription());
                });
    }


    @Scheduled(cron = "* * * * * *")
    public void sendSessionReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sessionStartTime = now.plusMinutes(15);

        // Calculate the time window (current time +15 minutes ±1 minute)
        LocalDate sessionDate = sessionStartTime.toLocalDate();
        LocalTime windowStart = sessionStartTime.toLocalTime().minusMinutes(1);
        LocalTime windowEnd = sessionStartTime.toLocalTime().plusMinutes(1);

        // Fetch bookings within the window
        List<Booking> bookings = bookingRepository.findBookingsForReminder(
                sessionDate,
                windowStart,
                windowEnd,
                Booking.Status.APPROVED
        );

        bookings.stream()
                .filter(booking -> !booking.isReminderSent())
                .forEach(booking -> {
                    TrainingSession session = booking.getTrainingSession();
                    emailService.sendReminderEmail(
                            booking.getUser().getEmail(),
                            session.getDescription(),
                            session.getDate(),
                            session.getStartTime(),
                            session.getMeetLink()

                    );
                    booking.setReminderSent(true);
                    bookingRepository.save(booking);
                });
    }

    public List<Booking> getBookingsByCoach() {
        UserInfo coach = getCurrentUser();
        return bookingRepository.finbookingsforTrainingSessionByCoach(coach.getId());
    }

    //get training session id from booking id


}
