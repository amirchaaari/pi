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

    @GetMapping("/retrieve-all-abonnements")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
    public List<Abonnement> getAllAbonnements() {
        return abonnementService.getAllAbonnements();
    }

    @GetMapping("/retrieve-abonnement/{id}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
    public Abonnement getAbonnementById(@PathVariable Long id) {
        return abonnementService.getAbonnementById(id);
    }


    @PutMapping("/update-abonnement/{id}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
    public Abonnement updateAbonnement(@PathVariable Long id, @RequestBody Abonnement abonnement) {
        return abonnementService.updateAbonnement(id, abonnement);
    }

    @DeleteMapping("/remove-abonnement/{id}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
    public void removeAbonnement(@PathVariable Long id) {
        abonnementService.deleteAbonnement(id);
    }

    @PutMapping("/renew-abonnement/{id}")
    public ResponseEntity<Abonnement> renewAbonnement(
            @PathVariable("id") Long id,
            @RequestParam("newEndDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newEndDate) {
        Abonnement updated = abonnementService.renewAbonnement(id, newEndDate);
        return ResponseEntity.ok(updated);
    }


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
