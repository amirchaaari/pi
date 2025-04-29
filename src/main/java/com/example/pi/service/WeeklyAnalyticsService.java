package com.example.pi.service;

import com.example.pi.entity.WeeklyAnalytics;
import com.example.pi.repository.DietProgramRepo;
import com.example.pi.repository.MealPlanRepo;
import com.example.pi.repository.RecipeRepo;
import com.example.pi.repository.WeekAnalyticsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class WeeklyAnalyticsService {
    @Autowired
    RecipeRepo recipeRepo;
    @Autowired
    MealPlanRepo mealPlanRepo;
    @Autowired
    DietProgramRepo dietProgramRepo;
    @Autowired
    WeekAnalyticsRepo weekAnalyticsRepo;

    @Scheduled(cron = "0 0 0 * * SUN") // Chaque dimanche à minuit

    public void analyzeWeeklyNutritionData() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        long totalRecipes = recipeRepo.countCreatedThisWeek(startOfWeek, endOfWeek);
        long totalMealPlans = mealPlanRepo.countCreatedThisWeek(startOfWeek, endOfWeek);
       List< String> mostUsedDiet = dietProgramRepo.findMostUsedDietProgramName();

        WeeklyAnalytics stats = new WeeklyAnalytics();
        stats.setWeek(today.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
        stats.setTotalRecipes(totalRecipes);
        stats.setTotalMealPlans(totalMealPlans);
        stats.setMostUsedDietType(mostUsedDiet.toString());

        weekAnalyticsRepo.save(stats);

        System.out.println(" weekly Analytics saved to database !");
    }
}



