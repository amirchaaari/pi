package com.example.pi.entity;

import jakarta.persistence.*;

@Entity
public class MealPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "idMealPlan")
    private long idMealPlan;
    private String dayOfWeek;
    private String description;
    @Column(name = "user_id") // lel GymGoer khtr nesthako fel methode personalisée
    private Long userId;
    @ManyToOne
    @JoinColumn(name = "DietProgramId", referencedColumnName = "idDiet")
    private DietProgram dietProgram;
    @ManyToOne
    @JoinColumn(name = "RecipeId", referencedColumnName = "idRecipe")
    private Recipe recipe;
}