package com.example.pi.repository;

import com.example.pi.entity.ClubOwner;
import com.example.pi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface ClubOwnerRepository extends JpaRepository<ClubOwner, Integer> {
    Optional<UserInfo> findByEmail(String email); // Use 'email' if that is the correct field for login

}

