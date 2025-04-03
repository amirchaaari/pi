package com.example.pi.interfaces.trainingSession;

import com.example.pi.entity.Review;
import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;

import java.util.List;

public interface IReviewService {
    public Review createReview(Long sessionId, Integer rating, String description);
    public List<Review> getSessionReviews(Long sessionId);
}
