package com.example.pi.repository.trainignSessionRepo;

import com.example.pi.entity.Review;
import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTrainingSessionId(Long sessionId);
    boolean existsByUserAndTrainingSession(UserInfo user, TrainingSession session);
}
