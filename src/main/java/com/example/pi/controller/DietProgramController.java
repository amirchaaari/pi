package com.example.pi.controller;

import com.example.pi.entity.DietProgram;
import com.example.pi.entity.Recipe;
import com.example.pi.service.IDietProgramService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/dietprogram")
public class DietProgramController {
    @Autowired
    IDietProgramService dietProgramService;
    @GetMapping("/retrieveAllDietPrograms")
    public List<DietProgram> retrieveAllDietPrograms() {
        List<DietProgram> dietProgramList = dietProgramService.retrieveAllDietPrograms();
        return dietProgramList;
    }
    //ajout
    @PostMapping("/addDietProgram")
    public String addDietProgram(DietProgram dietProgram) {
        dietProgramService.addDietProgram(dietProgram);
        return "diet program added successfully";
    }
    //modification
    @PutMapping("/updateDietProgram")
    public DietProgram updateDietProgram(@RequestBody DietProgram dietProgram) {
        DietProgram updatedDietProgram = dietProgramService.updateDietProgram(dietProgram);
        return updatedDietProgram;
    }
    //recupération taa Diet prog whd
    @GetMapping("/retrieveDietProgram/{dietId}")
    public DietProgram retrieveDietProgram(@PathVariable("dietId") long id) {
        return dietProgramService.retrieveDietProgram(id);
    }

    //supression d'une recette
    @DeleteMapping("/removeDietProgram/{DietProgramId}")
    public void removeDietProgram(@PathVariable("DietProgramId") long id) {
        dietProgramService.removeDietProgram(id);
    }
}
