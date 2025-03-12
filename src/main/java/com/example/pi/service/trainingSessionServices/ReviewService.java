package com.example.pi.service.trainingSessionServices;

import com.example.pi.entity.Review;
import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;
import com.example.pi.interfaces.trainingSession.IReviewService;
import com.example.pi.repository.UserInfoRepository;
import com.example.pi.repository.trainignSessionRepo.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final UserInfoRepository userInfoRepository;
    private final TrainingSessionService trainingSessionService;


    /*@Override
    public Review createReview(Review review, UserInfo user, TrainingSession session) {
        review.setUser(user);
        review.setTrainingSession(session);
        return reviewRepository.save(review);
    }*/

    @Override
    public Review createReview(Review review, Integer userID, Long sessionID) {
        UserInfo user = userInfoRepository.findById(userID).orElse(null);
        TrainingSession session = trainingSessionService.getSessionById(sessionID);
        review.setUser(user);
        review.setTrainingSession(session);
        reviewRepository.save(review);
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsBySession(Long sessionId) {
        return reviewRepository.findByTrainingSessionId(sessionId);
    }

    @Override
    public Review updateReview(Long id, Review review) {
        review.setId(id);
        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
