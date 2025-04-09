package com.example.pi.service;

import com.example.pi.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(Category category);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    Category updateCategory(Long id, Category categoryDetails);
    void deleteById(Long id);
}