package com.example.pi.interfaces.trainingSession;

import com.example.pi.entity.Review;
import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;

import java.util.List;

public interface IReviewService {
    Review createReview(Review review, UserInfo user, TrainingSession session);
    List<Review> getReviewsBySession(Long sessionId);
}
