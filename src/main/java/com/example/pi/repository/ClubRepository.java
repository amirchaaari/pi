package com.example.pi.repository;

import com.example.pi.entity.Club;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    Optional<Club> findByOwner_Id(Long ownerId);

}
