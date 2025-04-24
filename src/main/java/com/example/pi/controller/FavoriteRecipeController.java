package com.example.pi.controller;

import com.example.pi.entity.FavoriteRecipe;
import com.example.pi.service.FavoriteRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/recipe/favorites")
public class FavoriteRecipeController {

    @Autowired
    private FavoriteRecipeService favoriteRecipeService;

    // nrecuperi  les recettes favorites d'un utilisateur lkol
    @GetMapping("/recipeget/{userId}")
    public List<FavoriteRecipe> getFavoritesByUser(@PathVariable Long userId) {
        try {
            return favoriteRecipeService.getFavoritesByUser(userId.intValue());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    // najouty  recette lel favoris d'un utilisateur
    @PostMapping("/addfav")
    public FavoriteRecipe addFavorite(@RequestParam String email, @RequestParam Long recipeId) {
        try {
            return favoriteRecipeService.addFavorite(email, recipeId);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already in favorites", e);
        }
    }

    // tnahy  recette des favoris d'un utilisateur
    @DeleteMapping("/removefav/{id}")
    public void removeFavorite(@PathVariable Long id) {
        try {
            favoriteRecipeService.removeFavorite(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Favorite not found", e);
        }
    }
}
