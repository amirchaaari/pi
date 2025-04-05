package com.example.pi.service;

import com.example.pi.entity.Recipe;
import com.example.pi.repository.RecipeRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RecipeService implements IRecipeService {
    @Autowired
    RecipeRepo recipeRepo;
    @Override
    public List<Recipe> retrieveAllRecipes() {
        return List.of();
    }

    @Override
    public String addRecipe(Recipe recipe) {return "recipe added successfully";}

    @Override
    public Recipe updateRecipe(Recipe recipe) {
        return null;
    }

    @Override
    public Recipe retrieveRecipe(Long idRecipe) {
        return null;
    }

    @Override
    public void removeRecipe(Long idRecipe) {

    }
    @Override
    public List<Recipe> findAIRecipesByIngredient(String ingredient) {
        return List.of();
    }
    @Override
    public List<Recipe> addManyRecipes(List<Recipe> Recipes) {
        return List.of();
    }
}
