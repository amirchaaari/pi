package com.example.pi.repository.trainignSessionRepo;

import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
    // Inherits CRUD methods like save(), findById(), findAll(), deleteById()
    List<TrainingSession> findByCoachAndDateBetween(UserInfo coach, LocalDate start, LocalDate end);

    List<TrainingSession> findByCoach(UserInfo coach);
}
