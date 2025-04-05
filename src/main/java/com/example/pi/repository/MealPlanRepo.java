package com.example.pi.repository;

import com.example.pi.entity.MealPlan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MealPlanRepo extends CrudRepository<MealPlan, Long> {
    // Recherche par plusieurs userIds
    @Query("SELECT m FROM MealPlan m WHERE m.userId IN :userIds")
    List<MealPlan> findByMultipleUserIds(@Param("userIds") List<Long> userIds);

    // Recherche par plusieurs jours de la semaine
    @Query("SELECT m FROM MealPlan m WHERE m.dayOfWeek IN :daysOfWeek")
    List<MealPlan> findByMultipleDaysOfWeek(@Param("daysOfWeek") List<String> daysOfWeek);

    // Recherche bel userId we jour de la semaine
    @Query("SELECT m FROM MealPlan m WHERE m.userId = :userId AND m.dayOfWeek = :dayOfWeek")
    List<MealPlan> findByUserIdAndDayOfWeek(@Param("userId") Long userId, @Param("dayOfWeek") String dayOfWeek);

    // Recherche par plusieurs userIds w jours de la semaine
    @Query("SELECT m FROM MealPlan m WHERE m.userId IN :userIds AND m.dayOfWeek IN :daysOfWeek")
    List<MealPlan> findByUserIdsAndDaysOfWeek(@Param("userIds") List<Long> userIds, @Param("daysOfWeek") List<String> daysOfWeek);
}

