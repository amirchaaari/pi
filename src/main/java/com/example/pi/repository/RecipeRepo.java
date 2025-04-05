package com.example.pi.repository;

import com.example.pi.entity.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepo extends CrudRepository<Recipe, Long> {
}
