package com.example.pi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Entity
@Getter
@Setter
@Component
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "idRecipe")
    private long idRecipe;
    private String name;
    private String description;
    private String instructions;
    private int calories;
    private String ingredients;

}
