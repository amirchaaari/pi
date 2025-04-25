package com.example.pi.controller;

import com.example.pi.entity.Abonnement;
import com.example.pi.service.AbonnementService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/abonnements")
public class AbonnementController {

    private final AbonnementService abonnementService;

    // Récupérer tous les abonnements de l'utilisateur authentifié (Club Owner)
    @GetMapping("/retrieve-all-abonnements")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER') or hasRole('ROLE_ADMIN')")
    public List<Abonnement> getAllAbonnements() {
        // Récupère tous les abonnements pour le Club Owner authentifié et le admin
        return abonnementService.getAllAbonnements();
    }

    // Récupérer un abonnement par son ID (pour un Club Owner ou Admin)
    @GetMapping("/retrieve-abonnement/{id}")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER') or hasRole('ROLE_ADMIN')")
    public Abonnement getAbonnementById(@PathVariable Long id) {
        // Récupère un abonnement spécifique par ID
        return abonnementService.getAbonnementById(id);
    }


    // Mettre à jour un abonnement par Club Owners ou Admins
    @PutMapping("/update-abonnement/{id}")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER') or hasRole('ROLE_ADMIN')")
    public Abonnement updateAbonnement(@PathVariable Long id, @RequestBody Abonnement abonnement) {
        // Met à jour un abonnement existant
        return abonnementService.updateAbonnement(id, abonnement);
    }

    // Supprimer un abonnement par Club Owners ou Admins
    @DeleteMapping("/remove-abonnement/{id}")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER') or hasRole('ROLE_ADMIN')")
    public void removeAbonnement(@PathVariable Long id) {
        // Supprime l'abonnement spécifié
        abonnementService.deleteAbonnement(id);
    }

    @PutMapping("/renew-abonnement/{id}")
    public ResponseEntity<Abonnement> renewAbonnement(
            @PathVariable("id") Long id,
            @RequestParam("newEndDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newEndDate) {
        Abonnement updated = abonnementService.renewAbonnement(id, newEndDate);
        return ResponseEntity.ok(updated);
    }





    // Récupérer l'historique des abonnements pour l'utilisateur connecté
    @GetMapping("/user-history")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Abonnement> getUserAbonnementsHistory() {
        return abonnementService.getUserAbonnementsHistory();
    }


    @GetMapping("calculateRenewalRateForClub/{id}")
    public double calculateRenewalRateForClub(@PathVariable Long id) {
        return abonnementService.calculateRenewalRateForClub(id);
    }

    @GetMapping("analyzeClubPerformance/{id}")
    public Map<String, Object> analyzeClubPerformance(@PathVariable Long id) {
        return abonnementService.analyzeClubPerformance(id);
    }

}
