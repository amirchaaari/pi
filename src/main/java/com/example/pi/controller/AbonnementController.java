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

    // Récupérer tous les abonnements de l'utilisateur authentifié
    @GetMapping("/retrieve-all-abonnements")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Abonnement> getAllAbonnements() {
        return abonnementService.getAllAbonnements();
    }

    // Récupérer un abonnement par ID
    @GetMapping("/retrieve-abonnement/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Abonnement getAbonnementById(@PathVariable Long id) {
        return abonnementService.getAbonnementById(id);
    }

    // Ajouter un abonnement - réservé aux Club Owners ou ADMIN
    @PostMapping("/add-abonnement")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER') or hasRole('ROLE_ADMIN')")
    public Abonnement addAbonnement(@RequestBody Long packId) {
        return abonnementService.createAbonnement(packId);
    }

    // Mettre à jour un abonnement - réservé aux Club Owners ou ADMIN
    @PutMapping("/update-abonnement/{id}")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER') or hasRole('ROLE_ADMIN')")
    public Abonnement updateAbonnement(@PathVariable Long id, @RequestBody Abonnement abonnement) {
        return abonnementService.updateAbonnement(id, abonnement);
    }

    // Supprimer un abonnement - réservé aux Club Owners ou ADMIN
    @DeleteMapping("/remove-abonnement/{id}")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER') or hasRole('ROLE_ADMIN')")
    public void removeAbonnement(@PathVariable Long id) {
        abonnementService.deleteAbonnement(id);
    }
}
