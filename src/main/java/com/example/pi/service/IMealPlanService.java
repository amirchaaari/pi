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

    List<MealPlan> findMealPlansByUserIds(List<Long> userIds);

    List<MealPlan> findMealPlansByDaysOfWeek(List<String> daysOfWeek);

    List<MealPlan> findMealPlansByUserIdAndDayOfWeek(Long userId, String dayOfWeek);

    List<MealPlan> findMealPlansByUserIdsAndDaysOfWeek(List<Long> userIds, List<String> daysOfWeek);
}
