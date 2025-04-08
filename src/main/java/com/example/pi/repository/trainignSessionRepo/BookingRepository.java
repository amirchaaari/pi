package com.example.pi.repository.trainignSessionRepo;

import com.example.pi.entity.Booking;
import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByTrainingSessionId(Long sessionId);
    List<Booking> findByStatus(Booking.Status status);
    List<Booking> findByStatusAndTrainingSessionEndTimeBefore(Booking.Status status, LocalTime trainingSession_endTime);
    boolean existsByUserAndTrainingSession(UserInfo user, TrainingSession trainingSession);
    List<Booking> findByUser(UserInfo user);
    boolean existsByUserAndTrainingSessionAndStatus(UserInfo user, TrainingSession session, Booking.Status status);

    List<Booking> findByTrainingSessionAndStatus(TrainingSession session, Booking.Status status);
}
