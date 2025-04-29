package com.example.pi.repository;

import com.example.pi.entity.FavoriteRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FavoriteRecipeRepo extends CrudRepository<FavoriteRecipe, Long> {
    List<FavoriteRecipe> findByUserId(Long userId);
    boolean existsByUserEmailAndRecipeIdRecipe(String email, Long idRecipe);
}
