package com.example.pi.repository;

import com.example.pi.entity.Recipe;
import com.example.pi.entity.enumeration.MealType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepo extends CrudRepository<Recipe, Long> {

    // Recherche b au moins un ingrédient
    @Query("SELECT r FROM Recipe r WHERE " +
            "r.ingredients LIKE %:ingredient1% OR " +
            "r.ingredients LIKE %:ingredient2% OR " +
            "r.ingredients LIKE %:ingredient3%")
    List<Recipe> findByMultipleIngredients(
            @Param("ingredient1") String ingredient1,
            @Param("ingredient2") String ingredient2,
            @Param("ingredient3") String ingredient3);

    // Recherche bel ingrédient
    List<Recipe> findByIngredientsContaining(String ingredient);


    Optional<Recipe> findById(long id);  // staamlt optional bch ngerry les cas win mnlkwch recipe bel id lfleni

    @Query("SELECT r FROM Recipe r WHERE r.dietType = :dietType ORDER BY function('RAND')")
    List<Recipe> findTop3ByDietTypeRandom(@Param("dietType") String dietType);

    @Query("SELECT COUNT(r) FROM Recipe r WHERE r.creationDate >= :startOfWeek AND r.creationDate <= :endOfWeek")
    long countCreatedThisWeek(@Param("startOfWeek") LocalDate startOfWeek, @Param("endOfWeek") LocalDate endOfWeek);
    List<Recipe> findByNameContainingIgnoreCase(String name);
    List<Recipe> findByIngredientsContainingIgnoreCase(String ingredients);
    List<Recipe> findByMealType(MealType mealType); // sans IgnoreCase car enum
    ;

}
