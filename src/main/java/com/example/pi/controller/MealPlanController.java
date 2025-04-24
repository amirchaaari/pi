package com.example.pi.controller;

import com.example.pi.dto.MealPlanRequest;
import com.example.pi.entity.MealPlan;
import com.example.pi.service.IMealPlanService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
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
        System.out.println("Adding meal plan: " + mealPlan);
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
    //advanced chwaya
    // ycherchy mealplan par plusieurs userIds
    @GetMapping("/findByMultipleUserIds")
    public List<MealPlan> findMealPlansByUserIds(@RequestParam List<Long> userIds) {
        return mealPlanService.findMealPlansByUserIds(userIds);
    }

    // ycherchy mealplan    par plusieurs jours de la semaine
    @GetMapping("/findByMultipleDaysOfWeek")
    public List<MealPlan> findMealPlansByDaysOfWeek(@RequestParam List<String> daysOfWeek) {
        return mealPlanService.findMealPlansByDaysOfWeek(daysOfWeek);
    }

    // ycherchy mealplan bel  userId we jour de la semaine
    @GetMapping("/findByUserIdAndDayOfWeek")
    public List<MealPlan> findMealPlansByUserIdAndDayOfWeek(@RequestParam Long userId, @RequestParam String dayOfWeek) {
        return mealPlanService.findMealPlansByUserIdAndDayOfWeek(userId, dayOfWeek);
    }

    // ycherchy mealplan  par plusieurs userIds et jours de la semaine
    @GetMapping("/findByUserIdsAndDaysOfWeek")
    public List<MealPlan> findMealPlansByUserIdsAndDaysOfWeek(@RequestParam List<Long> userIds, @RequestParam List<String> daysOfWeek) {
        return mealPlanService.findMealPlansByUserIdsAndDaysOfWeek(userIds, daysOfWeek);
    }

    @PostMapping("/create")
    public ResponseEntity<MealPlan> createMealPlan(@RequestBody MealPlanRequest request) {
        MealPlan saved = mealPlanService.createMealPlan(request);
        return ResponseEntity.ok(saved);
    }

}

