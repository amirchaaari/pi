package com.example.pi.repository;

import com.example.pi.entity.ClubCreationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubCreationRequestRepository extends JpaRepository<ClubCreationRequest, Long> {
    // Méthode pour retrouver toutes les demandes en attente
    List<ClubCreationRequest> findByStatus(ClubCreationRequest.RequestStatus status);
}
