package com.example.pi.repository.trainignSessionRepo;

import com.example.pi.entity.Booking;
import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByTrainingSessionId(Long sessionId);
    List<Booking> findByStatus(Booking.Status status);
    List<Booking> findByStatusAndTrainingSessionEndTimeBefore(Booking.Status status, LocalTime trainingSession_endTime);
    boolean existsByUserAndTrainingSession(UserInfo user, TrainingSession trainingSession);
    List<Booking> findByUser(UserInfo user);

    boolean existsByUserAndTrainingSessionAndStatus(UserInfo user, TrainingSession session, Booking.Status status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.trainingSession.date = :sessionDate " +
            "AND b.trainingSession.startTime BETWEEN :startTime AND :endTime " +
            "AND b.status = :status")
    List<Booking> findBookingsForReminder(
            @Param("sessionDate") LocalDate sessionDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("status") Booking.Status status
    );

    @Query("SELECT b FROM Booking b WHERE b.trainingSession.date = :sessionDate AND b.status = :status")
    List<Booking> findByTrainingSessionDateAndStatus(
            @Param("sessionDate") LocalDate sessionDate,
            @Param("status") Booking.Status status);


    @Query("SELECT FUNCTION('DAYNAME', b.trainingSession.date) AS dayOfWeek, COUNT(b) " +
            "FROM Booking b " +
            "WHERE b.status = 'APPROVED' " +
            "GROUP BY FUNCTION('DAYNAME', b.trainingSession.date)")
    List<Object[]> countApprovedBookingsByDayOfWeek();

    @Query("SELECT HOUR(b.trainingSession.startTime) AS hour, COUNT(b) " +
            "FROM Booking b " +
            "WHERE b.status = 'APPROVED' " +
            "GROUP BY HOUR(b.trainingSession.startTime)")
    List<Object[]> countApprovedBookingsByHour();

    @Query("SELECT b FROM Booking b WHERE b.trainingSession.coach = :coach")
    List<Booking> findBookingsByCoach(@Param("coach") UserInfo coach);


    @Query("SELECT b FROM Booking b WHERE b.trainingSession.coach.id = :id")
    List<Booking> finbookingsforTrainingSessionByCoach(int id);

    int countByUser(UserInfo user);
}
