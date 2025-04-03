package com.example.pi.controller.TrainingSessionControllers;

import com.example.pi.entity.Review;
import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;
import com.example.pi.repository.UserInfoRepository;
import com.example.pi.repository.trainignSessionRepo.TrainingSessionRepository;
import com.example.pi.service.trainingSessionServices.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("training-sessions/{sessionId}/reviews")
@AllArgsConstructor
public class ReviewController {
    @Autowired
    private final ReviewService reviewService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Review createReview(
            @PathVariable Long sessionId, @RequestBody Review review) {
        return reviewService.createReview(sessionId, review.getRating(), review.getDescription());
    }

    @GetMapping("/all")
    public List<Review> getSessionReviews(@PathVariable Long sessionId) {
        return reviewService.getSessionReviews(sessionId);
    }

    @PutMapping("/{reviewId}/update")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Review updateReview(@RequestBody Review review) {
        return reviewService.updateReview(review.getId(),review);
    }

    @DeleteMapping("{reviewId}/delete")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public void deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}