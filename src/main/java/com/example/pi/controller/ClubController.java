package com.example.pi.controller;

import com.example.pi.entity.Club;
import com.example.pi.entity.ClubCreationRequest;
import com.example.pi.service.ClubService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;

    // Permet à tous les utilisateurs d'afficher tous les clubs
    @GetMapping("/retrieve-all-clubs")
    public List<Club> getAllClubs() {
        return clubService.getAllClubs();
    }

    // Permet à tous les utilisateurs d'afficher un club spécifique par son id
    @GetMapping("/retrieve-club/{id}")
    public Club getClubById(@PathVariable Long id) {
        return clubService.getClubById(id);
    }

    // Seul un ClubOwner peut ajouter un club
    @PostMapping("/add-club")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER')")
    public Club addClub(@RequestBody Club club) {
        return clubService.createClub(club);
    }

    // Seul un ClubOwner peut mettre à jour un club
    @PutMapping("/update-club/{id}")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER')")
    public Club updateClub(@PathVariable Long id, @RequestBody Club club) {
        return clubService.updateClub(id, club);
    }

    // Seul un ClubOwner peut supprimer un club
    @DeleteMapping("/remove-club/{id}")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER')")
    public void removeClub(@PathVariable Long id) {
        clubService.deleteClub(id);
    }

    // Seul un ClubOwner peut affecter un sport à un club
    @PostMapping("/{clubId}/sports/{sportId}")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER')")
    public Club affecterSportToClub(@PathVariable Long clubId, @PathVariable Long sportId) {
        return clubService.affecterSportToClub(clubId, sportId);
    }

    // Permet de soumettre une demande de création de club (accessible uniquement par un ClubOwner)
    @PostMapping("/submit-creation-request")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER')")
    public Club submitClubCreationRequest(@RequestBody ClubCreationRequest request) {
        // Soumettre la demande de création de club et récupérer le Club créé
        ClubCreationRequest creationRequest = clubService.submitClubCreationRequest(request);

        // Approuver la demande et créer un Club
        return clubService.approveClubCreationRequest(creationRequest.getId());
    }

    // Afficher les demandes en attente (accessible uniquement par un administrateur)
    @GetMapping("/admin/pending-requests")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ClubCreationRequest> getPendingRequests() {
        return clubService.getPendingClubCreationRequests();
    }

    // Permet d'approuver une demande de création de club (accessible uniquement par un administrateur)
    @PostMapping("/admin/approve/{requestId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Club approveClubCreationRequest(@PathVariable Long requestId) {
        return clubService.approveClubCreationRequest(requestId);
    }

    // Permet de refuser une demande de création de club (accessible uniquement par un administrateur)
    @PostMapping("/admin/reject/{requestId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void rejectClubCreationRequest(@PathVariable Long requestId) {
        clubService.rejectClubCreationRequest(requestId);
    }
}
