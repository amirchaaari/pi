package com.example.pi.dto;

import com.example.pi.entity.enumeration.MealType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealPlanRequest {
    private String dayOfWeek;
    private String description;
    private Long userId;
    private String userEmail; // For direct assignment if needed
    private MealType mealType;
    private Integer mealOrder; //  mithel 1=Breakfast, 2=Morning snack.
    private Long dietProgramId;
    private Long recipeId;
}