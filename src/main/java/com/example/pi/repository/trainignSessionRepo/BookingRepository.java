package com.example.pi.repository.trainignSessionRepo;

import com.example.pi.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByTrainingSessionId(Long sessionId);
}
