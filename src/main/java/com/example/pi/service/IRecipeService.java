package com.example.pi.service;

import com.example.pi.entity.Recipe;

import java.util.List;

public interface IRecipeService {
    List<Recipe> retrieveAllRecipes();

    String addRecipe(Recipe recipe);

    Recipe updateRecipe(Recipe recipe);

    Recipe retrieveRecipe(Long idRecipe);

    void removeRecipe(Long idRecipe);

    List<Recipe> findAIRecipesByIngredient(String ingredient);

    List<Recipe> addManyRecipes(List<Recipe> Recipes);

    List<Recipe> findRecipesByIngredients(List<String> ingredients);
}
