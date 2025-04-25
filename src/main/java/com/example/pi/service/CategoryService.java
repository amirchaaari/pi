/*
package com.example.pi.service;

import com.example.pi.entity.Category;
import com.example.pi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Save a new category
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    // Get all categories
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    // Get a category by ID
    public Category findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElse(null); // Return null if not found
    }

    // Update a category
    public Category update(Long id, Category categoryDetails) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            Category categoryToUpdate = existingCategory.get();
            categoryToUpdate.setName(categoryDetails.getName());
            categoryToUpdate.setDescription(categoryDetails.getDescription());
            // Update other fields as necessary
            return categoryRepository.save(categoryToUpdate);
        }
        return null; // Return null if the category does not exist
    }

    // Delete a category by ID
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    // Check if a category exists by ID
    public boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }
}*/
