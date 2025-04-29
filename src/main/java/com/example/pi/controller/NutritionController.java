package com.example.pi.controller;

import com.example.pi.service.NutritionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/nutrition")
public class NutritionController {
    @Autowired
    private NutritionService nutritionService;

    @GetMapping("/recommendation/{goal}")
    public String getNutritionRecommendation(@PathVariable String goal) {
        return nutritionService.getNutritionRecommendation(goal);
    }
}
