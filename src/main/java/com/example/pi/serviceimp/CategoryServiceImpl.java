package com.example.pi.serviceimp;

import com.example.pi.entity.Category;
import com.example.pi.repository.CategoryRepository; // Ensure correct import
import com.example.pi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category); // Save the new category
    }

    @Override
    public Category getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElse(null); // Handle case where category is not found
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll(); // Retrieve all categories
    }

    @Override
    public Category updateCategory(Long id, Category categoryDetails) {
        if (categoryRepository.existsById(id)) {
            categoryDetails.setId(id); // Set the ID to ensure correct update
            return categoryRepository.save(categoryDetails);
        }
        return null; // Or throw an exception if needed
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id); // Delete the category by ID
    }
}