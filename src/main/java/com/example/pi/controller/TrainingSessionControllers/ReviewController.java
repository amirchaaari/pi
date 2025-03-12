package com.example.pi.controller.TrainingSessionControllers;

import com.example.pi.entity.Review;
import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;
import com.example.pi.repository.UserInfoRepository;
import com.example.pi.repository.trainignSessionRepo.TrainingSessionRepository;
import com.example.pi.service.trainingSessionServices.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-sessions/{sessionId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final TrainingSessionRepository trainingSessionRepository;
    private final UserInfoRepository userInfoRepository;

    public ReviewController(ReviewService reviewService,
                            TrainingSessionRepository trainingSessionRepository,
                            UserInfoRepository userInfoRepository) {
        this.reviewService = reviewService;
        this.trainingSessionRepository = trainingSessionRepository;
        this.userInfoRepository = userInfoRepository;
    }

    /*@PostMapping
    public ResponseEntity<Review> createReview(
            @PathVariable Long sessionId,
            @RequestBody Review review,
            Authentication authentication) {

        // Get authenticated user
        String username = authentication.getName();
        UserInfo user = userInfoRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get training session
        TrainingSession session = trainingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Training session not found"));

        Review createdReview = reviewService.createReview(review, user, session);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }*/

    @GetMapping
    public ResponseEntity<List<Review>> getSessionReviews(@PathVariable Long sessionId) {
        List<Review> reviews = reviewService.getReviewsBySession(sessionId);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long reviewId,
            @RequestBody Review review) {
        Review updatedReview = reviewService.updateReview(reviewId, review);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}