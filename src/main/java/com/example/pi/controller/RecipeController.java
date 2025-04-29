package com.example.pi.controller;

import com.example.pi.dto.RecipeRequest;
import com.example.pi.entity.Recipe;
import com.example.pi.service.IRecipeService;
import com.example.pi.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/recipe")
public class RecipeController {

    @Autowired
    IRecipeService recipeService;

    @Autowired
    ImageService imageService;

    @GetMapping("/retrieveAllRecipes")
    public List<Recipe> retrieveAllRecipes() {
        return recipeService.retrieveAllRecipes();
    }

    @PostMapping(value = "/add-recipe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addRecipe(@ModelAttribute RecipeRequest request,
                                       @RequestParam("image") MultipartFile imageFile) {
        try {
            String result = recipeService.addRecipe(request, imageFile);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while uploading the image: " + e.getMessage());
        }
    }


    @PutMapping("/update-recipe")
    public ResponseEntity<Recipe> updateRecipe(@RequestBody RecipeRequest recipeRequest) {
        Optional<Recipe> existingRecipe = recipeService.findRecipeById(recipeRequest.getIdRecipe());
        if (existingRecipe.isPresent()) {
            Recipe updatedRecipe = existingRecipe.get();
            updatedRecipe.setName(recipeRequest.getName());
            updatedRecipe.setDescription(recipeRequest.getDescription());
            updatedRecipe.setCalories(recipeRequest.getCalories());
            updatedRecipe.setIngredients(recipeRequest.getIngredients());
            updatedRecipe.setYoutubeUrl(recipeRequest.getYoutubeUrl());

            // Appel de la méthode calculatePreparationTime dans le service
            updatedRecipe.setPreparationTime(recipeService.calculatePreparationTime(recipeRequest));

            Recipe savedRecipe = recipeService.updateRecipe(updatedRecipe);
            return ResponseEntity.ok(savedRecipe);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/retrieve-recipe/{RecipeId}")
    public Recipe retrieveRecipe(@PathVariable("RecipeId") Long id) {
        return recipeService.retrieveRecipe(id);
    }

    @DeleteMapping("/remove-recipe/{RecipeId}")
    public void removeRecipe(@PathVariable("RecipeId") Long id) {
        recipeService.removeRecipe(id);
    }

    @PostMapping("/add-manyrecipes")
    public List<Recipe> addManyRecipes(@RequestBody List<Recipe> recipes) {
        return recipeService.addManyRecipes(recipes);
    }

    @GetMapping("/findByIngredient")
    public List<Recipe> findAIRecipesByIngredient(@RequestParam String ingredient) {
        return recipeService.findAIRecipesByIngredient(ingredient);
    }

    @GetMapping("/findByMultipleIngredients")
    public List<Recipe> findRecipesByIngredients(@RequestParam List<String> ingredients) {
        return recipeService.findRecipesByIngredients(ingredients);
    }
    @GetMapping("/recipes/search")
    public List<Recipe> searchRecipes(@RequestParam("q") String query) {
        return recipeService.findByNameOrMealTypeOrIngredientsContaining(query);
    }

}
