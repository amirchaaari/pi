package com.example.pi.service;

import com.example.pi.dto.RecipeRequest;
import com.example.pi.entity.Recipe;
import com.example.pi.entity.SuggestedRecipe;
import com.example.pi.entity.enumeration.MealType;
import com.example.pi.repository.RecipeRepo;
import com.example.pi.repository.SuggestedRecipeRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RecipeService implements IRecipeService {
    @Autowired
    RecipeRepo recipeRepo;
    @Autowired
    SuggestedRecipeRepo suggestedRecipeRepo;
    @Autowired
    ImageService imageService; //cloudinary

    @Override
    public String addRecipe(RecipeRequest request, MultipartFile imageFile) throws IOException {
        String imageUrl = imageService.uploadImage(imageFile);

        Recipe recipe = new Recipe();
        recipe.setName(request.getName());
        recipe.setDescription(request.getDescription());
        recipe.setInstructions(request.getInstructions());
        recipe.setCalories(request.getCalories());
        recipe.setIngredients(request.getIngredients());
        recipe.setDietType(request.getDietType());
        recipe.setImageUrl(imageUrl);
        recipe.setYoutubeUrl(request.getYoutubeUrl());
        if (request.getMealType() != null) {
            recipe.setMealType(MealType.valueOf(request.getMealType().toUpperCase()));  // yconverti lel majuscule bch tji maa enum
        }

        int hours = request.getPreparationHours();
        int minutes = request.getPreparationMinutes();
        recipe.setPreparationTime(Duration.ofHours(hours).plusMinutes(minutes));
        recipeRepo.save(recipe);
        return "Recipe added successfully !";
    }

    //calcule taa duration de preparation lel recipe
    @Override
    public Duration calculatePreparationTime(RecipeRequest recipeRequest) {
        int totalMinutes = recipeRequest.getPreparationHours() * 60 + recipeRequest.getPreparationMinutes();
        return Duration.ofMinutes(totalMinutes);
    }

    @Override
    public List<Recipe> retrieveAllRecipes() {
        return (List<Recipe>) recipeRepo.findAll();
    }


    @Override
    public Recipe updateRecipe(Recipe recipe) {
        return recipeRepo.save(recipe);
    }

    @Override
    public Recipe retrieveRecipe(Long idRecipe) {
        return recipeRepo.findById(idRecipe)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    @Override
    public void removeRecipe(Long idRecipe) {
        recipeRepo.deleteById(idRecipe);
    }

    @Override
    public List<Recipe> findAIRecipesByIngredient(String ingredient) {
        return recipeRepo.findByIngredientsContaining(ingredient);
    }

    @Override
    public List<Recipe> addManyRecipes(List<Recipe> Recipes) {
        return (List<Recipe>) recipeRepo.saveAll(Recipes);
    }

    @Override
    public List<Recipe> findRecipesByIngredients(List<String> ingredients) {
        List<Recipe> recipes = new ArrayList<>();
        for (String ingredient : ingredients) {
            recipes.addAll(recipeRepo.findByIngredientsContaining(ingredient));
        }

        return recipes;
    }

    @Override
    public Optional<Recipe> findRecipeById(Long idRecipe) {
        Optional<Recipe> recipe = recipeRepo.findById(idRecipe);
        return recipe;
    }

    @Scheduled(cron = "0 0 8 * * *") // tous les jours à 6h
    public void generateDailyRecipeSuggestions() {
        List<String> dietTypes = List.of("Keto", "Vegan", "Mediterranean");

        for (String type : dietTypes) {
            List<Recipe> randomRecipes = recipeRepo.findTop3ByDietTypeRandom(type);

            if (!randomRecipes.isEmpty()) {
                SuggestedRecipe suggestion = new SuggestedRecipe();
                suggestion.setDietType(type);
                suggestion.setRecipes(randomRecipes);
                suggestion.setDate(LocalDate.now());

                suggestedRecipeRepo.save(suggestion);
            }
        }

        System.out.println("recipe suggestion generated successfully .");
    }
    @Override
    public List<Recipe> findByNameOrMealTypeOrIngredientsContaining(String query) {
        List<Recipe> results = new ArrayList<>();


        results.addAll(recipeRepo.findByNameContainingIgnoreCase(query));


        results.addAll(recipeRepo.findByIngredientsContainingIgnoreCase(query));


        try {
            MealType mealType = MealType.valueOf(query.toUpperCase()); // convertit "lunch" -> "LUNCH"
            results.addAll(recipeRepo.findByMealType(mealType));
        } catch (IllegalArgumentException e) {
            // query n'est pas un MealType valide
        }

        // tevity e doublon
        return results.stream().distinct().toList();
    }



}