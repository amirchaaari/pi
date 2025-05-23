package com.example.pi.repository;

import com.example.pi.entity.Status;
import com.example.pi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByEmail(String email); // Use 'email' if that is the correct field for login

    void deleteByEmail(String email);
    Optional<UserInfo> findByResetToken(String resetToken);
    Optional<UserInfo> findByVerificationToken(String verificationToken);

    List<UserInfo> findByRoles(String roleCoach);

    @Query("SELECT c, COALESCE(AVG(r.rating), 0.0), COUNT(r) " +
            "FROM UserInfo c " +
            "LEFT JOIN c.trainingSessions ts " +
            "LEFT JOIN ts.reviews r " +
            "WHERE c.roles = :roles " +
            "GROUP BY c " +
            "ORDER BY COALESCE(AVG(r.rating), 0.0) DESC, COUNT(r) DESC")
    List<Object[]> findCoachesWithAvgRating(@Param("roles") String roles);
    Optional<UserInfo> findByName(String name);
    List<UserInfo> findAllByStatus(Status status);

    List<UserInfo> findByLastSessionBefore(LocalDateTime dateTime);

    @Query("SELECT u.email FROM UserInfo u")
    List<String> findAllEmails();



}
