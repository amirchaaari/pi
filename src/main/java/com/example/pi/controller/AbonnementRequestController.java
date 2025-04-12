package com.example.pi.controller;

import com.example.pi.entity.Abonnement;
import com.example.pi.entity.AbonnementRequest;
import com.example.pi.service.AbonnementRequestService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/abonnement-requests")
@AllArgsConstructor
public class AbonnementRequestController {

    private final AbonnementRequestService requestService;

    // Faire une demande d’abonnement (GymGoer)
    @PostMapping("/request/{packId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public AbonnementRequest createRequest(@PathVariable Long packId) {
        return requestService.createRequest(packId);
    }

    // Le Club Owner approuve une demande
    @PutMapping("/approve/{requestId}")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER')")
    public Abonnement approveRequest(@PathVariable Long requestId) {
        return requestService.validateRequest(requestId);
    }

    // Le Club Owner rejette une demande
    @PutMapping("/reject/{requestId}")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER')")
    public AbonnementRequest rejectRequest(@PathVariable Long requestId) {
        return requestService.rejectRequest(requestId);  // Appel à la méthode de rejet dans le service
    }
    // Le Club Owner récupère les demandes
    @GetMapping("/club-owner/requests")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER')")
    public List<AbonnementRequest> getRequests() {
        return requestService.getRequestsForOwner();
    }
}
