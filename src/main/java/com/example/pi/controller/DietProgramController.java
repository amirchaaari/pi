package com.example.pi.controller;

import com.example.pi.dto.DietProgramRequest;
import com.example.pi.entity.DietProgram;
import com.example.pi.service.IDietProgramService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/dietprogram")
public class DietProgramController {

    @Autowired
    private IDietProgramService dietProgramService;

    // Récupérer tous les programmes de régime
    @GetMapping("/retrieveAllDietPrograms")
    public List<DietProgram> retrieveAllDietPrograms() {
        List<DietProgram> dietProgramList = dietProgramService.retrieveAllDietPrograms();
        return dietProgramList;
    }

    // Ajouter un programme de régime (utilise DietProgramRequest pour DTO)
    @PostMapping("/addDietProgram")
    public ResponseEntity<DietProgram> addDietProgram(@RequestBody DietProgramRequest dietProgramRequest) {
        // Convertir DietProgramRequest en DietProgram dans le service
        DietProgram dietProgram = dietProgramService.addDietProgram(dietProgramRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(dietProgram);  // 201 Created
    }


    // Mettre à jour un programme de régime
    @PutMapping("/updateDietProgram")
    public DietProgram updateDietProgram(@RequestBody DietProgramRequest dietProgramRequest) {
        if (dietProgramRequest.getIdDiet() == null) {
            throw new IllegalArgumentException("Diet program ID is required for the update.");
        }
        DietProgram updatedDietProgram = dietProgramService.updateDietProgram(dietProgramRequest);
        return updatedDietProgram;
    }

    // Récupérer un programme de régime par ID
    @GetMapping("/retrieveDietProgram/{dietId}")
    public DietProgram retrieveDietProgram(@PathVariable("dietId") long id) {
        return dietProgramService.retrieveDietProgram(id);
    }

    // Supprimer un programme de régime
    @DeleteMapping("/removeDietProgram/{DietProgramId}")
    public void removeDietProgram(@PathVariable("DietProgramId") long id) {
        dietProgramService.removeDietProgram(id);
    }

    // Comparer deux programmes de régime
    @GetMapping("/compareDietPrograms")
    public String comparePrograms(@RequestParam Long programId1, @RequestParam Long programId2) {
        return dietProgramService.compareNutritionPrograms(programId1, programId2);
    }

}
