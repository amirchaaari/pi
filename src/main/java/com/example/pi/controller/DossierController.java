package com.example.pi.controller;

import com.example.pi.entity.Dossier;
import com.example.pi.entity.Meeting;
import com.example.pi.service.IDossierService;
import com.example.pi.service.IMeetingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/dossier")
public class DossierController {
    IDossierService dossierService;
    @GetMapping("/retrieve-all-dossiers")
    public List<Dossier> getDossiers() {
        List<Dossier> listDossiers = dossierService.retrieveAllDossiers();
        return listDossiers;

    }


    @PostMapping("/add-dossier")
    public Dossier addDossier(@RequestBody Dossier c) {
        Dossier dossier = dossierService.addDossier(c);
        return dossier;
    }

    @DeleteMapping("/remove-dossier/{dossier-id}")
    public void removeDossier(@PathVariable("dossier-id") Long dossierId) {
        dossierService.removeDossier(dossierId);
    }

    @PutMapping("/update-dossier")
    public Dossier updateDossier(@RequestBody Dossier c) {
        Dossier dossier= dossierService.updateDossier(c);
        return dossier;
    }
}
