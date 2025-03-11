package com.example.pi.controller;

import com.example.pi.entity.Abonnement;
import com.example.pi.service.AbonnementService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/abonnements")
public class AbonnementController {

    private final AbonnementService abonnementService;

    @GetMapping("/retrieve-all-abonnements")
    public List<Abonnement> getAllAbonnements() {
        return abonnementService.getAllAbonnements();
    }

    @GetMapping("/retrieve-abonnement/{id}")
    public Abonnement getAbonnementById(@PathVariable Long id) {
        return abonnementService.getAbonnementById(id);
    }

    @PostMapping("/add-abonnement")
    public Abonnement addAbonnement(@RequestBody Abonnement abonnement) {
        return abonnementService.createAbonnement(abonnement);
    }

    @PutMapping("/update-abonnement/{id}")
    public Abonnement updateAbonnement(@PathVariable Long id, @RequestBody Abonnement abonnement) {
        return abonnementService.updateAbonnement(id, abonnement);
    }

    @DeleteMapping("/remove-abonnement/{id}")
    public void removeAbonnement(@PathVariable Long id) {
        abonnementService.deleteAbonnement(id);
    }
}
