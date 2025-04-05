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
        return (List<MealPlan>) mealPlanRepo.findAll();
    }

    @Override
    public MealPlan addMealPlan(MealPlan Diet) {
        return mealPlanRepo.save(Diet);
    }

    @Override
    public MealPlan updateMealPlan(MealPlan Diet) {
        return mealPlanRepo.save(Diet);
    }

    @Override
    public MealPlan retrieveMealPlan(Long idMealPlan) {
        return mealPlanRepo.findById(idMealPlan).orElse(null);//ken lmealplan mfameech menou yrajaa null
    }

    @Override
    public void removeMealPlan(Long idMealPlan) {
        mealPlanRepo.deleteById(idMealPlan);
    }

    @Override
    public List<MealPlan> generatePersonalizedMealPlan(Long userId, int durationInDays) {
        return List.of();//to be continued mzelt mkmlthech!!!!!!!!!!!!!
    }
    @Override
    public List<MealPlan> addManyMealPlans(List<MealPlan> MealPlans) {
        return (List<MealPlan>) mealPlanRepo.saveAll(MealPlans);
    }
    @Override
    public List<MealPlan> findMealPlansByUserIds(List<Long> userIds) {
        return mealPlanRepo.findByMultipleUserIds(userIds);
    }

    @Override
    public List<MealPlan> findMealPlansByDaysOfWeek(List<String> daysOfWeek) {
        return mealPlanRepo.findByMultipleDaysOfWeek(daysOfWeek);
    }

    @Override
    public List<MealPlan> findMealPlansByUserIdAndDayOfWeek(Long userId, String dayOfWeek) {
        return mealPlanRepo.findByUserIdAndDayOfWeek(userId, dayOfWeek);
    }

    @Override
    public List<MealPlan> findMealPlansByUserIdsAndDaysOfWeek(List<Long> userIds, List<String> daysOfWeek) {
        return mealPlanRepo.findByUserIdsAndDaysOfWeek(userIds, daysOfWeek);
    }
}

