package com.example.pi.repository;

import com.example.pi.entity.Club;
import com.example.pi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findByOwner(UserInfo owner);
    List<Club> findByStatus(Club.RequestStatus status);
    Optional<Club> findByOwner_Id(Long ownerId);
}
