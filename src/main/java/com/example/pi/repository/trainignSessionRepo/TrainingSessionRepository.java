package com.example.pi.repository.trainignSessionRepo;

import com.example.pi.entity.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
    // Inherits CRUD methods like save(), findById(), findAll(), deleteById()
    List<TrainingSession> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
