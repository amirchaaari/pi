package com.example.pi.repository.PromotionRepository;

import com.example.pi.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    // Additional query methods can be defined here if needed
}