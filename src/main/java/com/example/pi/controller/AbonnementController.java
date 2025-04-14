package com.example.pi.controller;

import com.example.pi.entity.Abonnement;
import com.example.pi.service.AbonnementService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
