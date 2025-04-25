package com.example.pi.serviceimp;

import com.example.pi.entity.Promotion;
import com.example.pi.repository.PromotionRepository;
import com.example.pi.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Override
    public Promotion createPromotion(Promotion promotion) {
        return promotionRepository.save(promotion); // Save the new promotion
    }

    @Override
    public Promotion getPromotionById(Long id) {
        Optional<Promotion> promotion = promotionRepository.findById(id);
        return promotion.orElse(null); // Handle case where promotion is not found
    }

    @Override
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll(); // Retrieve all promotions
    }

    @Override
    public Promotion updatePromotion(Long id, Promotion promotionDetails) {
        if (promotionRepository.existsById(id)) {
            promotionDetails.setId(id); // Set the ID to ensure correct update
            return promotionRepository.save(promotionDetails);
        }
        return null; // Or throw an exception if needed
    }

    @Override
    public void deleteById(Long id) {
        promotionRepository.deleteById(id); // Delete the promotion by ID
    }
}