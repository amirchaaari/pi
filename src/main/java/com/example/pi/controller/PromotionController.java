package com.example.pi.controller;

import com.example.pi.entity.Promotion;
import com.example.pi.service.PromotionService; // Ensure correct import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @GetMapping
    public List<Promotion> getAllPromotions() {
        return promotionService.getAllPromotions(); // Retrieve all promotions
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promotion> getPromotionById(@PathVariable Long id) {
        Promotion promotion = promotionService.getPromotionById(id);
        if (promotion != null) {
            return ResponseEntity.ok(promotion); // Return promotion if found
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }

    @PostMapping
    public ResponseEntity<Promotion> createPromotion(@RequestBody Promotion promotion) {
        Promotion createdPromotion = promotionService.createPromotion(promotion); // Save new promotion
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPromotion); // Return 201 status
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promotion> updatePromotion(@PathVariable Long id, @RequestBody Promotion promotionDetails) {
        Promotion updatedPromotion = promotionService.updatePromotion(id, promotionDetails);
        if (updatedPromotion != null) {
            return ResponseEntity.ok(updatedPromotion); // Return updated promotion
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        promotionService.deleteById(id); // Delete promotion by ID
        return ResponseEntity.noContent().build(); // Return 204 status
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // Handle exceptions
    }
}