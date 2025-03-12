package com.example.pi.service;

import com.example.pi.entity.Promotion;
import java.util.List;

public interface PromotionService {
    Promotion createPromotion(Promotion promotion); // Create a new promotion
    Promotion getPromotionById(Long id); // Retrieve a promotion by ID
    List<Promotion> getAllPromotions(); // Get all promotions
    Promotion updatePromotion(Long id, Promotion promotionDetails); // Update an existing promotion
    void deleteById(Long id); // Delete a promotion by ID
}