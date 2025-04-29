package com.example.pi.service.trainingSessionServices;

import com.example.pi.dto.CoachDTO;
import com.example.pi.entity.Booking;
import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;
import com.example.pi.interfaces.trainingSession.ITrainingSessionService;
import com.example.pi.repository.UserInfoRepository;
import com.example.pi.repository.trainignSessionRepo.BookingRepository;
import com.example.pi.repository.trainignSessionRepo.ReviewRepository;
import com.example.pi.repository.trainignSessionRepo.TrainingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingSessionService implements ITrainingSessionService {
    @Autowired
    private final TrainingSessionRepository trainingSessionRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private GoogleCalendarService googleCalendarService;
    @Autowired
    private final BookingRepository bookingRepository;
    @Autowired
    private final ReviewRepository reviewRepository;

    public TrainingSession createSession(TrainingSession trainingSession) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        UserInfo coach = userInfoRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Coach not found with email: " + currentUserEmail));
        trainingSession.setCoach(coach);
        if (trainingSession.getStartTime().isAfter(trainingSession.getEndTime())) {
            throw new RuntimeException("Start time must be before end time");
        }
            String startTime = trainingSession.getDate() + "T" + trainingSession.getStartTime() + ":00"; // Format start time as 'yyyy-MM-ddTHH:mm:ss'
            String endTime = trainingSession.getDate() + "T" + trainingSession.getEndTime() + ":00"; // Format end time as 'yyyy-MM-ddTHH:mm:ss'

            String meetLink = googleCalendarService.createEvent(trainingSession.getDescription(), "Training session with coach", startTime, endTime);

            trainingSession.setMeetLink(meetLink);
        return trainingSessionRepository.save(trainingSession);
    }


    @Override
    public TrainingSession updateSession(Long id, TrainingSession trainingSession) {
        TrainingSession existingSession = trainingSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Training session not found with id: " + id));

        if(!id.equals(existingSession.getId())) { return existingSession; }
        existingSession.setId(id);
        existingSession.setDate(trainingSession.getDate());
        existingSession.setDescription(trainingSession.getDescription());
        existingSession.setStartTime(trainingSession.getStartTime());
        existingSession.setEndTime(trainingSession.getEndTime());
        existingSession.setSport(trainingSession.getSport());
        existingSession.setCoach(trainingSession.getCoach());
        existingSession.setReviews(trainingSession.getReviews());
        existingSession.setExercices(trainingSession.getExercices());
        existingSession.setBookings(trainingSession.getBookings());

        return trainingSessionRepository.save(existingSession);
    }

    @Override
    public void deleteSession(Long id) {
        trainingSessionRepository.deleteById(id);
    }

    @Override
    public TrainingSession getSessionById(Long id) {
        return trainingSessionRepository.findById(id).orElse(null);
    }

    @Override
    public List<TrainingSession> getAllSessions() {
        return trainingSessionRepository.findAllOrderByDateAndTimeAsc();
    }


    public List<CoachDTO> getRecommendedCoaches() {
        String role = "ROLE_COACH";
        List<Object[]> results = userInfoRepository.findCoachesWithAvgRating(role);
        return results.stream().map(result -> {
            UserInfo coach = (UserInfo) result[0];
            Double avgRating = (Double) result[1];
            Long reviewCount = (Long) result[2];
            return new CoachDTO(
                    coach.getId(),
                    coach.getName(),
                    coach.getEmail(),
                    coach.getRoles(),
                    avgRating != null ? avgRating : 0.0,
                    reviewCount != null ? reviewCount : 0
            );
        }).collect(Collectors.toList());
    }
    public List<TrainingSession> getSessionsInRangeForCurrentCoach(LocalDate start, LocalDate end) {
        // Get current logged-in user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Fetch the coach by email
        UserInfo coach = userInfoRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Coach not found with email: " + currentUserEmail));

        // Query training sessions within range for that coach
        return trainingSessionRepository.findByCoachAndDateBetween(coach, start, end);
    }
    public List<TrainingSession> getSessionsByCurrentCoach() {
        // Get the current logged-in user's email (coach) from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); // Get the current user's email

        // Find the coach by email from the database
        UserInfo coach = userInfoRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Coach not found with email: " + currentUserEmail));

        // Retrieve the training sessions associated with the current coach
        return trainingSessionRepository.findByCoach(coach);
    }

    public List<Booking> getBookingsByCoachId() {
        // Get the current logged-in user's email (the coach)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Find the coach by email from the database
        UserInfo coach = userInfoRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Coach not found with email: " + currentUserEmail));

        // Retrieve the bookings associated with the current coach
        return bookingRepository.findBookingsByCoach(coach);
    }




}

