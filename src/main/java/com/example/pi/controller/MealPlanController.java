package com.example.pi.controller;

import com.example.pi.entity.MealPlan;
import com.example.pi.entity.Recipe;
import com.example.pi.service.IMealPlanService;
import com.example.pi.service.IRecipeService;
import com.example.pi.service.MealPlanService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/mealplan")
public class MealPlanController {
    @Autowired
    IMealPlanService mealPlanService;

    //recupération des plan nutritionnel
    @GetMapping("/retrieve-allMealPlans")
    public List<MealPlan> retrieveAllMealPlans() {
        List<MealPlan> mealPlans = mealPlanService.retrieveAllMealPlans();
        return mealPlans;
    }
    //ajout
    @PostMapping("/add-mealPlan")
    public String addMealPlan(@RequestBody MealPlan mealPlan) {
        mealPlanService.addMealPlan(mealPlan);
        return "mealplan added successfully";
    }
    //modification
    @PutMapping("/update-mealplan")
    public String updateMealPlan(@RequestBody MealPlan mealPlan) {
        MealPlan mealPlanUpdated = mealPlanService.updateMealPlan(mealPlan);
        return "mealplan updated successfully";
    }
    //recupération taa meal whd
    @GetMapping("/retrieve-mealPlan")
    public MealPlan retrieveMealPlan(@RequestParam long id) {
        MealPlan mealPlan = mealPlanService.retrieveMealPlan(id);
        return mealPlan;
    }

    //supression d'un meal plan
    @DeleteMapping("/remove-mealPlan/{MealPlanId}")
    public void removeMealPlan(@PathVariable ("MealPlanId") long MealPlanId) {
        mealPlanService.removeMealPlan(MealPlanId);
    }

    //ajout de plusieur Mealplans
    @PostMapping("/add-manyMealPlans")
    public List<MealPlan> addManyMealPlans(@RequestBody List<MealPlan> mealPlanList) {
        return mealPlanService.addManyMealPlans(mealPlanList);
    }
}
