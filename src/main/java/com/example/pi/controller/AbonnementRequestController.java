package com.example.pi.controller;

import com.example.pi.entity.Abonnement;
import com.example.pi.entity.AbonnementRequest;
import com.example.pi.service.AbonnementRequestService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/abonnement-requests")
@AllArgsConstructor
public class AbonnementRequestController {

    private final AbonnementRequestService requestService;

    @PostMapping("/request/{packId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public AbonnementRequest createRequest(@PathVariable Long packId) {
        System.out.println("Creating request for pack ID: " + packId);
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
