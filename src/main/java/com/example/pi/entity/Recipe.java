package com.example.pi.entity;

import com.example.pi.entity.DurationConverter.DurationToLongConverter;
import com.example.pi.entity.enumeration.MealType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Component
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "idRecipe")
    private Long idRecipe;
    private String name;
    private String description;
    private String instructions;
    private int calories;
    private String ingredients;
    //image url
    private String imageUrl;
    @Column(name = "diet_type")
    private String dietType;
    @Column(name = "creation_date")
    private LocalDate creationDate;
    @Enumerated(EnumType.STRING)  // Persiste sous forme de chaîne
    @Column(name = "meal_type")
    private MealType mealType;

    @PrePersist
    public void prePersist() {
        this.creationDate = LocalDate.now();
    }
    @JsonProperty("image")
    public String getImageUrl() {
        return imageUrl;
    }

    @Convert(converter = DurationToLongConverter.class)
    @Column(name = "preparation_time")
    private Duration preparationTime;
    @Column(name = "youtube_url")
    private String youtubeUrl;





}
