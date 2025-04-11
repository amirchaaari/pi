package com.example.pi.service;

import com.example.pi.entity.Command;
import com.example.pi.entity.Promotion;
import com.example.pi.repository.CommandRepository;
import com.example.pi.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private CommandRepository commandRepository;


    public List<Promotion> findAll() {
        return promotionRepository.findAll();
    }

    public Optional<Promotion> findById(Long id) {
        return promotionRepository.findById(id);
    }

    public Promotion save(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    public void deleteById(Long id) {
        promotionRepository.deleteById(id);
    }

    public Command applyPromotion(Long commandId, String promoCode) {
        Optional<Command> commandOpt = commandRepository.findById(commandId);
        Optional<Promotion> promotionOpt = promotionRepository.findByCode(promoCode);

        if (commandOpt.isPresent() && promotionOpt.isPresent()) {
            Command command = commandOpt.get();
            Promotion promotion = promotionOpt.get();

            if (promotion.getExpiryDate().isAfter(LocalDate.now())) {
                double discount = promotion.getDiscountPercentage();
                double newTotal = command.getTotal() - (command.getTotal() * discount / 100);
                command.setTotal(newTotal);
                commandRepository.save(command);
            }
        }

        return commandOpt.orElse(null);
    }


    @Scheduled(cron = "0 0 9 * * ?") // Runs every day at 9 AM
    public void checkForAlmostExpiredPromotions() {
        LocalDate today = LocalDate.now();
        LocalDate alertDate = today.plusDays(3); // Alert for promotions expiring in 3 days

        List<Promotion> almostExpiredPromotions = promotionRepository.findAll().stream()
                .filter(promotion -> promotion.getExpiryDate() != null &&
                        promotion.getExpiryDate().isAfter(today) &&
                        promotion.getExpiryDate().isBefore(alertDate))
                .toList();

        for (Promotion promotion : almostExpiredPromotions) {
            System.out.println("ALERT: Promotion \"" + promotion.getTitle() + "\" is expiring soon on " + promotion.getExpiryDate());
            // You can replace this with actual notification logic (e.g., email or SMS)
        }
    }



}