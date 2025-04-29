package com.example.pi.controller;

import com.example.pi.dto.MealPlanRequest;

import com.example.pi.entity.MealPlan;
import com.example.pi.entity.enumeration.MealType;
import com.example.pi.service.IMealPlanService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    // Get all meal plans
    @GetMapping("/retrieve-allMealPlans")
    public ResponseEntity<List<MealPlan>> retrieveAllMealPlans() {
        List<MealPlan> mealPlans = mealPlanService.retrieveAllMealPlans();
        return ResponseEntity.ok(mealPlans);
    }

    // Add meal plan
    @PostMapping("/add-mealPlan")
    public ResponseEntity<String> addMealPlan(@RequestBody MealPlanRequest request) {
        MealPlan created = mealPlanService.createMealPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Meal plan added successfully for user: " + created.getUserEmail());
    }

    // Update meal plan

    @PutMapping("/update-mealplan/{id}")
    public ResponseEntity<String> updateMealPlan(
            @PathVariable Long id,
            @RequestBody MealPlanRequest request) {
        MealPlan existing = mealPlanService.retrieveMealPlan(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        // Update fields mel request
        existing.setDayOfWeek(request.getDayOfWeek());
        existing.setDescription(request.getDescription());
        existing.setMealType(request.getMealType());
        existing.setMealOrder(request.getMealOrder());

        MealPlan updated = mealPlanService.updateMealPlan(existing);
        return ResponseEntity.ok("Meal plan updated successfully for: " + updated.getUserEmail());
    }


    @GetMapping("/retrieve-mealPlan/{id}")
    public ResponseEntity<MealPlan> retrieveMealPlan(@PathVariable Long id) {
        MealPlan mealPlan = mealPlanService.retrieveMealPlan(id);
        return mealPlan != null ?
                ResponseEntity.ok(mealPlan) :
                ResponseEntity.notFound().build();
    }


    @DeleteMapping("/remove-mealPlan/{id}")
    public ResponseEntity<String> removeMealPlan(@PathVariable Long id) {
        MealPlan existing = mealPlanService.retrieveMealPlan(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        mealPlanService.removeMealPlan(id);
        return ResponseEntity.ok("Meal plan deleted successfully for: " + existing.getUserEmail());
    }


    @PostMapping("/add-manyMealPlans")
    public ResponseEntity<List<MealPlan>> addManyMealPlans(@RequestBody List<MealPlanRequest> requests) {
        // You'll need to implement batch creation in service
        List<MealPlan> created = requests.stream()
                .map(mealPlanService::createMealPlan)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @GetMapping("/findByMultipleUserIds")
    public ResponseEntity<List<MealPlan>> findMealPlansByUserIds(@RequestParam List<Long> userIds) {
        return ResponseEntity.ok(mealPlanService.findMealPlansByUserIds(userIds));
    }

    @GetMapping("/findByMultipleDaysOfWeek")
    public ResponseEntity<List<MealPlan>> findMealPlansByDaysOfWeek(
            @RequestParam List<String> daysOfWeek,
            @RequestParam(required = false) MealType mealType) { // New optional filter
        List<MealPlan> result = mealPlanService.findMealPlansByDaysOfWeek(daysOfWeek);
        if (mealType != null) {
            result = result.stream()
                    .filter(mp -> mp.getMealType() == mealType)
                    .toList();
        }
        return ResponseEntity.ok(result);
    }

    //creation

    @PostMapping("/create")
    public ResponseEntity<MealPlan> createMealPlan(@RequestBody MealPlanRequest request) {
        MealPlan saved = mealPlanService.createMealPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // trecuperi mealplan hasb type
    @GetMapping("/by-meal-type/{mealType}")
    public ResponseEntity<List<MealPlan>> getByMealType(@PathVariable MealType mealType) {
        List<MealPlan> result = mealPlanService.retrieveAllMealPlans().stream()
                .filter(mp -> mp.getMealType() == mealType)
                .toList();
        return ResponseEntity.ok(result);
    }
}