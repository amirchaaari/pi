package com.example.pi.service;

import com.example.pi.entity.Recipe;
import com.example.pi.repository.RecipeRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class RecipeService implements IRecipeService {
    @Autowired
    RecipeRepo recipeRepo;
    @Override
    public List<Recipe> retrieveAllRecipes() {
        return (List<Recipe>) recipeRepo.findAll();
    }

    @Override
    public String addRecipe(Recipe recipe) {
        recipeRepo.save(recipe);
        return "recipe added successfully";}

    @Override
    public Recipe updateRecipe(Recipe recipe) {
        return recipeRepo.save(recipe);
    }

    @Override
    public Recipe retrieveRecipe(Long idRecipe) {
        return recipeRepo.findById(idRecipe).orElse(null);  // trajaa null ken recette mch mwjouda
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
    //// mzel mkmlch advanced fonctionnality
//    public List<Recipe> findRecipesByIngredients(List<String> ingredients, int minCalories, int maxCalories) {
//        List<Recipe> recipes = new ArrayList<>();
//
//        // Récupération des recettes contenant les ingrédients spécifiés
//        for (String ingredient : ingredients) {
//            recipes.addAll(recipeRepo.findByIngredientsContaining(ingredient));
//        }
//        // Filtrage des recettes selon les calories
//        List<Recipe> filteredRecipes = new ArrayList<>();
//        for (Recipe recipe : recipes) {
//            if (recipe.getCalories() >= minCalories && recipe.getCalories() <= maxCalories) {
//                filteredRecipes.add(recipe);
//            }
//        }
//
//        return filteredRecipes;
//    }
}

