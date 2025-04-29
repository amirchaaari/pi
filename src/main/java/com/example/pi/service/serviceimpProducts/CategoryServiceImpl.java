package com.example.pi.service.serviceimpProducts;

import com.example.pi.entity.Category;
import com.example.pi.repository.CategoryRepository;
import com.example.pi.service.CategoryService;
import com.example.pi.service.ImageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ImageService imageService;

    @Override
    @Transactional
    public Category createCategory(Category category) {
        category.setImageUrl(category.getImageUrl());
        return categoryRepository.save(category); // Save the new category
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll(); // Retrieve all categories
    }

    @Override
    @Transactional
    public Category updateCategory(Long id, Category categoryDetails) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }

        // Retrieve the existing category
        Category existingCategory = categoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Category not found with id: " + id));

        // Update fields
        existingCategory.setName(categoryDetails.getName());
        existingCategory.setDescription(categoryDetails.getDescription());


        // Handle image if present
        if (categoryDetails.getImageUrl() != null) {
            existingCategory.setImageUrl(categoryDetails.getImageUrl());
        }

        return categoryRepository.save(existingCategory);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id); // Delete the category by ID
    }

//    public String saveImage(MultipartFile imageFile) {
//        try {
//            String directoryPath = "C:/Users/dalih/images"; // Ensure this path is correct
//            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
//            File targetFile = new File(directoryPath, fileName);
//            imageFile.transferTo(targetFile);
//
//            // Log the complete URL
//            String imageUrl = "/images/" + fileName; // Adjust according to your server's URL structure
//            System.out.println("Image URL: " + imageUrl);
//            return imageUrl;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null; // Handle the error appropriately
//        }
//    }

    public String saveImage(MultipartFile imageFile) {
        try {
            String directoryPath = "C:/Users/dalih/images";
            // Create directory if it doesn't exist
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate unique filename
            String fileName = "category_" + System.currentTimeMillis() +
                    imageFile.getOriginalFilename().substring(
                            imageFile.getOriginalFilename().lastIndexOf(".")
                    );

            // Save file
            File targetFile = new File(directory, fileName);
            imageFile.transferTo(targetFile);

            // Return relative path (important!)
            return "/images/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save image", e);
        }
    }
}