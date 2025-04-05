package com.example.pi.repository;

import com.example.pi.entity.MealPlan;
import org.springframework.data.repository.CrudRepository;

public interface MealPlanRepo extends CrudRepository<MealPlan, Long> {
}
