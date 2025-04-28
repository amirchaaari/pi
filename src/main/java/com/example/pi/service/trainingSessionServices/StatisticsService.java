package com.example.pi.service.trainingSessionServices;

import com.example.pi.entity.Booking;
import com.example.pi.entity.Review;
import com.example.pi.entity.UserInfo;
import com.example.pi.interfaces.trainingSession.IReviewService;
import com.example.pi.repository.UserInfoRepository;
import com.example.pi.repository.trainignSessionRepo.BookingRepository;
import com.example.pi.repository.trainignSessionRepo.ReviewRepository;
import com.example.pi.repository.trainignSessionRepo.TrainingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final TrainingSessionRepository trainingSessionRepository;
    private final UserInfoRepository userInfoRepository;
    private final BookingService bookingService;

    private final ReviewService reviewService;

    private UserInfo getCurrentCoach() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userInfoRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Map<String, Long> getBookingsByDayOfWeekForCurrentCoach() {
        UserInfo coach = getCurrentCoach();
        List<Booking> bookings = bookingRepository.findAll();

        return bookings.stream()
                .filter(b -> b.getTrainingSession().getCoach().equals(coach))
                .collect(Collectors.groupingBy(
                        b -> b.getTrainingSession().getDate().getDayOfWeek().toString(),
                        Collectors.counting()
                ));
    }

    public Map<Integer, Long> getBookingsByHourForCurrentCoach() {
        UserInfo coach = getCurrentCoach();
        List<Booking> bookings = bookingRepository.findAll();

        return bookings.stream()
                .filter(b -> b.getTrainingSession().getCoach().equals(coach))
                .collect(Collectors.groupingBy(
                        b -> b.getTrainingSession().getStartTime().getHour(),
                        Collectors.counting()
                ));
    }

    public Map<Long, Double> getAverageRatingPerSessionForCurrentCoach() {
        UserInfo coach = getCurrentCoach();
        List<Review> reviews = reviewRepository.findAll();

        return reviews.stream()
                .filter(r -> r.getTrainingSession().getCoach().equals(coach))
                .collect(Collectors.groupingBy(
                        r -> r.getTrainingSession().getId(),
                        Collectors.averagingInt(Review::getRating)
                ));
    }

    public Map<Long, Long> getReviewCountPerSessionForCurrentCoach() {
        UserInfo coach = getCurrentCoach();
        List<Review> reviews = reviewRepository.findAll();

        return reviews.stream()
                .filter(r -> r.getTrainingSession().getCoach().equals(coach))
                .collect(Collectors.groupingBy(
                        r -> r.getTrainingSession().getId(),
                        Collectors.counting()
                ));
    }


    public Map<String, Object> getCoachStatistics() {
        List<Booking> bookings = bookingService.getBookingsByCoach();
        List<Review> reviews = reviewService.getReviewsByCoachId();

        Map<String, Long> bookingsByDay;
        Map<Integer, Long> bookingsByHour;


        Map<String, Object> statistics = new HashMap<>();

        // Bookings by day
        bookingsByDay = bookings.stream()
                .collect(Collectors.groupingBy(
                        b -> b.getTrainingSession().getDate().getDayOfWeek().toString(),
                        Collectors.counting()
                ));
        statistics.put("bookingsByDay", bookingsByDay);

        // Bookings by hour
        bookingsByHour = bookings.stream()
                .collect(Collectors.groupingBy(
                        b -> b.getTrainingSession().getStartTime().getHour(),
                        Collectors.counting()
                ));
        statistics.put("bookingsByHour", bookingsByHour);

        // Session statistics
        Map<Long, SessionStats> sessionStats = new HashMap<>();
        reviews.forEach(review -> {
            Long sessionId = review.getTrainingSession().getId();
            sessionStats.computeIfAbsent(sessionId, k -> new SessionStats())
                    .addReview(review.getRating());
        });
        statistics.put("sessionStatistics", sessionStats);

        // Overall statistics
        statistics.put("totalBookings", bookings.size());
        statistics.put("totalReviews", reviews.size());
        statistics.put("averageRating", reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0));

        return statistics;
    }

    // Inner class to track session statistics
    private static class SessionStats {
        private int reviewCount = 0;
        private double totalRating = 0;
        private double averageRating = 0;

        public void addReview(double rating) {
            reviewCount++;
            totalRating += rating;
            averageRating = totalRating / reviewCount;
        }

        public int getReviewCount() {
            return reviewCount;
        }

        public double getAverageRating() {
            return averageRating;
        }
    }
}
