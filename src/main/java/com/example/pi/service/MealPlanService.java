package com.example.pi.service;

import com.example.pi.entity.MealPlan;
import com.example.pi.repository.MealPlanRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MealPlanService implements IMealPlanService {
    @Autowired
    MealPlanRepo mealPlanRepo;
    @Override
    public List<MealPlan> retrieveAllMealPlans() {
        return List.of();
    }

    @Override
    public MealPlan addMealPlan(MealPlan Diet) {
        return null;
    }

    @Override
    public MealPlan updateMealPlan(MealPlan Diet) {
        return null;
    }

    @Override
    public MealPlan retrieveMealPlan(Long idMealPlan) {
        return null;
    }

    @Override
    public void removeMealPlan(Long idMealPlan) {

    }

    @Override
    public List<MealPlan> generatePersonalizedMealPlan(Long userId, int durationInDays) {
        return List.of();
    }
    @Override
    public List<MealPlan> addManyMealPlans(List<MealPlan> MealPlans) {
        return List.of();
    }}
