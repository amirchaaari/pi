package com.example.pi.controller.Promotion;

import com.example.pi.entity.Promotion;
import com.example.pi.service.PromotionService.PromotionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @Transactional
    @GetMapping("/get-all")
    public List<Promotion> getAllPromotions() {
        return promotionService.findAll();
    }

    // Get a promotion by ID
    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<Promotion> getPromotionById(@PathVariable Long id) {
        Optional<Promotion> promotion = promotionService.findById(id);
        return promotion.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Create a new promotion
    @Transactional
    @PostMapping("/add-promotion")
    public ResponseEntity<Promotion> createPromotion(@RequestBody Promotion promotion) {
        Promotion savedPromotion = promotionService.save(promotion);
        return new ResponseEntity<>(savedPromotion, HttpStatus.CREATED);
    }

    // Update an existing promotion
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<Promotion> updatePromotion(@PathVariable Long id, @RequestBody Promotion promotion) {
        if (!promotionService.findById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        promotion.setId(id); // Set the ID to ensure correct update
        Promotion updatedPromotion = promotionService.save(promotion);
        return ResponseEntity.ok(updatedPromotion);
    }

    // Delete a promotion
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        if (!promotionService.findById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        promotionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/apply")
    @Transactional
    public ResponseEntity<String> applyPromotion(@PathVariable Long id) {
        try {
            promotionService.applyPromotion(id);
            return ResponseEntity.ok("Promotion applied successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}