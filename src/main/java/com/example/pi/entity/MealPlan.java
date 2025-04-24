package com.example.pi.entity;

import com.example.pi.entity.enumeration.MealType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Entity
@Component
@Setter
@Getter
public class MealPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "idMealPlan")
    private Long idMealPlan;
    private String dayOfWeek;
    private String description;
    @Column(name = "user_id") // lel GymGoer khtr nesthako fel methode personalisée
    private Long userId;
    @Enumerated(EnumType.STRING)
    private MealType mealType;
    @ManyToOne
    @JoinColumn(name = "DietProgramId", referencedColumnName = "idDiet")
    private DietProgram dietProgram;
    @ManyToOne
    @JoinColumn(name = "RecipeId", referencedColumnName = "idRecipe")
    private Recipe recipe;
    @Column(name = "creation_date")
    private LocalDate creationDate;
    @PrePersist
    public void prePersist() {
        this.creationDate = LocalDate.now();
    }

}