package com.example.pi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;

@Getter
@Setter
public class RecipeRequest {
    private Long idRecipe;
    private String name;
    private String description;
    private String instructions;
    private int calories;
    private String ingredients;
    private String dietType;
    private MultipartFile image;
    //hedhy lel uploaded images
    @JsonProperty("image")
    private String imageUrl;
    private int preparationHours;//lel duration bel heurs
    private int preparationMinutes;//lel duration minutes

    private Duration preparationTime;
    private String mealType;
    private String youtubeUrl;


}

