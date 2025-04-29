package com.example.pi.repository.trainignSessionRepo;

import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {

    List<TrainingSession> findByCoachAndDateBetween(UserInfo coach, LocalDate start, LocalDate end);

    List<TrainingSession> findByCoach(UserInfo coach);

    @Query("SELECT t FROM TrainingSession t ORDER BY t.date ASC, t.startTime ASC")
    List<TrainingSession> findAllOrderByDateAndTimeAsc();
}
