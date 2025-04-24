package com.example.pi.service;

import com.example.pi.dto.RecipeRequest;
import com.example.pi.entity.Recipe;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public interface IRecipeService {
    String addRecipe(RecipeRequest request, MultipartFile imageFile) throws IOException;

    //calcule taa duration de preparation lel recipe
    Duration calculatePreparationTime(RecipeRequest recipeRequest);

    List<Recipe> retrieveAllRecipes();


    Recipe updateRecipe(Recipe recipe);

    Recipe retrieveRecipe(Long idRecipe);

    void removeRecipe(Long idRecipe);

    List<Recipe> findAIRecipesByIngredient(String ingredient);

    List<Recipe> addManyRecipes(List<Recipe> Recipes);

    List<Recipe> findRecipesByIngredients(List<String> ingredients);
    Optional<Recipe> findRecipeById(Long idRecipe );

    List<Recipe> findByNameOrMealTypeOrIngredientsContaining(String query);
}
