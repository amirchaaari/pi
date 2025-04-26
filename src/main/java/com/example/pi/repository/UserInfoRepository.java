package com.example.pi.repository;

import com.example.pi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByEmail(String email); // Use 'email' if that is the correct field for login

    void deleteByEmail(String email);
    Optional<UserInfo> findByResetToken(String resetToken);
    Optional<UserInfo> findByVerificationToken(String verificationToken);
    List<UserInfo> findByLastSessionBefore(LocalDateTime dateTime);


}
