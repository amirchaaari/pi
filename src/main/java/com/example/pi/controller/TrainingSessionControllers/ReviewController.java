/*
package com.example.pi.controller.TrainingSessionControllers;

import com.example.pi.entity.Review;
import com.example.pi.service.trainingSessionServices.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/training-sessions")
@AllArgsConstructor
public class ReviewController {
    @Autowired
    private final ReviewService reviewService;

    @PostMapping("/{sessionId}/reviews/create")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Review createReview(
            @PathVariable Long sessionId, @RequestBody Review review) {
        return reviewService.createReview(sessionId, review.getRating(), review.getDescription());
    }

    @GetMapping("/{sessionId}/reviews/all")
    public List<Review> getSessionReviews(@PathVariable Long sessionId) {
        return reviewService.getSessionReviews(sessionId);
    }


    @GetMapping("/reviews")
    @PreAuthorize("hasAuthority('ROLE_COACH')")
    public ResponseEntity<List<Review>> getReviewsByCoachId() {
        List<Review> reviews = reviewService.getReviewsByCoachId();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }



    @PutMapping("/{sessionId}/reviews/{reviewId}/update")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Review updateReview(@RequestBody Review review) {
        return reviewService.updateReview(review.getId(),review);
    }

    @DeleteMapping("/{sessionId}/reviews/{reviewId}/delete")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public void deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}*/
