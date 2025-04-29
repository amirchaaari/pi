package com.example.pi.controller;

import com.example.pi.entity.Dossier;
import com.example.pi.entity.Meeting;
import com.example.pi.service.IDossierService;
import com.example.pi.service.IMeetingService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/dossier")
public class DossierController {
    IDossierService dossierService;
    @GetMapping("/retrieve-all-dossiers")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Dossier> getDossiers() {
        List<Dossier> listDossiers = dossierService.retrieveAllDossiers();
        return listDossiers;

    }

    @GetMapping("/{dossierId}/meetings")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Meeting> getMeetingsByFolderId(@PathVariable Long dossierId) {
        // Récupère le dossier via ton service
        Dossier dossier = dossierService.retrieveDossier(dossierId);
        if (dossier == null) {
            return new ArrayList<>();
        }
        // Retourne la liste des meetings (même vide si pas de meetings)
        return new ArrayList<>(dossier.getMeetings());  // Conversion Set à List si nécessaire
    }


    @GetMapping("/retrieve-dossier/{dossier-id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Dossier retrieveDossier(@PathVariable("dossier-id") Long dossierId) {
        return dossierService.retrieveDossier(dossierId);
    }


    @PostMapping("/add-dossier")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Dossier addDossier(@RequestBody Dossier c) {
        Dossier dossier = dossierService.addDossier(c);
        return dossier;
    }

    @DeleteMapping("/remove-dossier/{dossier-id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void removeDossier(@PathVariable("dossier-id") Long dossierId) {
        dossierService.removeDossier(dossierId);
    }

    @PutMapping("/update-dossier")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Dossier updateDossier(@RequestBody Dossier c) {
        Dossier dossier= dossierService.updateDossier(c);
        return dossier;
    }
    @GetMapping("/gender-stats")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Map<String, Object> getGenderStats() {
        return dossierService.getGenderStats();
    }

    @GetMapping("/medicalfolders")
    public Map<String, Long> getMedicalFolderStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalMedicalFolders", dossierService.getTotalMedicalFolders());
        return stats;
    }


}
