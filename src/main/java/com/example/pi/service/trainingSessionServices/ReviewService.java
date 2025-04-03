package com.example.pi.service.trainingSessionServices;

import com.example.pi.entity.Booking;
import com.example.pi.entity.Review;
import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;
import com.example.pi.interfaces.trainingSession.IReviewService;
import com.example.pi.repository.UserInfoRepository;
import com.example.pi.repository.trainignSessionRepo.BookingRepository;
import com.example.pi.repository.trainignSessionRepo.ReviewRepository;
import com.example.pi.repository.trainignSessionRepo.TrainingSessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService{

    @Autowired
    private final ReviewRepository reviewRepository;
    @Autowired
    private final BookingRepository bookingRepository;
    @Autowired
    private final TrainingSessionRepository trainingSessionRepository;
    @Autowired
    private final UserInfoRepository userInfoRepository;

    @Transactional
    public Review createReview(Long sessionId, Integer rating, String description) {
        UserInfo user = getCurrentUser();
        TrainingSession session = trainingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        validateReviewEligibility(user, session);
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Review review = new Review();
        review.setUser(user);
        review.setTrainingSession(session);
        review.setCreatedAt(LocalDateTime.now());
        review.setRating(rating);
        review.setDescription(description);

        return reviewRepository.save(review);
    }

    private void validateReviewEligibility(UserInfo user, TrainingSession session) {
        // Check session has ended
        if (session.getEndTime().isAfter(LocalTime.from(LocalDateTime.now()))) {
            throw new IllegalStateException("Session hasn't ended yet");
        }

        // Check user attended the session
        boolean hasApprovedBooking = bookingRepository.existsByUserAndTrainingSessionAndStatus(
                user,
                session,
                Booking.Status.APPROVED
        );

        if (!hasApprovedBooking) {
            throw new IllegalStateException("User didn't attend this session");
        }

        // Check existing review
        if (reviewRepository.existsByUserAndTrainingSession(user, session)) {
            throw new IllegalStateException("Already reviewed this session");
        }


    }

    public List<Review> getSessionReviews(Long sessionId) {
        return reviewRepository.findByTrainingSessionId(sessionId);
    }

    @Transactional
    public Review updateReview(Long reviewId,Review review) {
        review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        validateReviewOwnership(review);
        validateRating(review.getRating());
        review.setRating(review.getRating());
        review.setDescription(review.getDescription());
        return reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        validateReviewOwnershipOrAdmin(review);
        reviewRepository.delete(review);
    }

    private void validateReviewOwnership(Review review) {
        UserInfo currentUser = getCurrentUser();
        if (!review.getUser().equals(currentUser)) {
            throw new AccessDeniedException("You don't own this review");
        }
    }

    private void validateReviewOwnershipOrAdmin(Review review) {
        UserInfo currentUser = getCurrentUser();
        boolean isAdmin = currentUser.getRoles().contains("ROLE_ADMIN");

        if (!review.getUser().equals(currentUser) && !isAdmin) {
            throw new AccessDeniedException("Not authorized to modify this review");
        }
    }

    private void validateRating(Integer rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }

    private UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userInfoRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
