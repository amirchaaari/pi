package com.example.pi.repository;

import com.example.pi.entity.Recipe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepo extends CrudRepository<Recipe, Long> {
    //recherche b at least ingredient whd
    @Query  ("SELECT r FROM Recipe r WHERE " +
            "r.ingredients LIKE %:ingredient1% OR " +
            "r.ingredients LIKE %:ingredient2% OR " +
            "r.ingredients LIKE %:ingredient3%")
    List<Recipe> findByMultipleIngredients(
            @Param("ingredient1") String ingredient1,
            @Param("ingredient2") String ingredient2,
            @Param("ingredient3") String ingredient3);
// recherche b ingredients whd
    List<Recipe> findByIngredientsContaining(String ingredient);  // Recherche des recettes hasb l ingrédient
}
