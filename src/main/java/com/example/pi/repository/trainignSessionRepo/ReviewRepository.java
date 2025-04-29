package com.example.pi.repository.trainignSessionRepo;

import com.example.pi.entity.Review;
import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTrainingSessionId(Long sessionId);
    boolean existsByUserAndTrainingSession(UserInfo user, TrainingSession session);

    @Query("SELECT r.trainingSession.id, AVG(r.rating) " +
            "FROM Review r " +
            "GROUP BY r.trainingSession.id")
    List<Object[]> averageRatingPerSession();

    @Query("SELECT r.trainingSession.id, COUNT(r) " +
            "FROM Review r " +
            "GROUP BY r.trainingSession.id")
    List<Object[]> countReviewsPerSession();



    @Query("SELECT r FROM Review r WHERE r.trainingSession.coach.id = ?1")
    List<Review> findByTrainingSession_Coach(int coachId);

    int countByUser(UserInfo user);
}
