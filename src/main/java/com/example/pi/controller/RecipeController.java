package com.example.pi.controller;

import com.example.pi.entity.MealPlan;
import com.example.pi.entity.Recipe;
import com.example.pi.service.IRecipeService;
import com.example.pi.service.RecipeService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/recipe")
public class RecipeController {
    @Autowired
    IRecipeService recipeService;
    //recupération des recettes
    @GetMapping("/retrieveAllRecipes")
    public List<Recipe> retrieveAllRecipes() {
        List<Recipe> recipes = recipeService.retrieveAllRecipes();
        return recipes;
    }
    //ajout
    @PostMapping("/add-recipe")
    public String addRecipe(@RequestBody Recipe recipe) {
        recipeService.addRecipe(recipe);
        return "recipe added successfully";
    }

    //modification
    @PutMapping("/update-recipe")
    public Recipe updateRecipe(@RequestBody Recipe recipe) {
        Recipe updatedRecipe = recipeService.updateRecipe(recipe);
        return updatedRecipe;
    }
    //recupération taa recette wahda
   @GetMapping("/retrieve-recipe")
    public Recipe retrieveRecipe(long id) {
        Recipe recipe = recipeService.retrieveRecipe(id);
        return recipe;
   }
   //supression d'une recette
   @DeleteMapping("/remove-recipe/{RecipeId}")
    public void removeRecipe(@PathVariable("RecipeId") long id) {
        recipeService.removeRecipe(id);
   }

    //ajout de plusieur recette
    @PostMapping("/add-manyrecipes")
    public List<Recipe> addManyRecipes(@RequestBody List<Recipe> recipes) {
        return recipeService.addManyRecipes(recipes);
    }
    // Recherche des recettes selon les ingrédients
    @GetMapping("/findByIngredient")
    public List<Recipe> findAIRecipesByIngredient(@RequestParam String ingredient) {
        return recipeService.findAIRecipesByIngredient(ingredient);
    }
    @GetMapping("/findByMultipleIngredients")//akther men ingredient
    public List<Recipe> findRecipesByIngredients(@RequestParam List<String> ingredients) {
        return recipeService.findRecipesByIngredients(ingredients);
    }


}
