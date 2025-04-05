package com.example.pi.service;

import com.example.pi.entity.MealPlan;

import java.util.List;

public interface IMealPlanService {
    List<MealPlan> retrieveAllMealPlans();

    MealPlan addMealPlan(MealPlan Diet);

    MealPlan updateMealPlan(MealPlan Diet);

    MealPlan retrieveMealPlan(Long idMealPlan);

    void removeMealPlan(Long idMealPlan);

    List<MealPlan> generatePersonalizedMealPlan(Long userId, int durationInDays);

    List<MealPlan> addManyMealPlans(List<MealPlan> MealPlans);
}
