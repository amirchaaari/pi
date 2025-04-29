package com.example.pi.repository;

import com.example.pi.entity.SuggestedRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuggestedRecipeRepo extends JpaRepository<SuggestedRecipe, Long> {
}
