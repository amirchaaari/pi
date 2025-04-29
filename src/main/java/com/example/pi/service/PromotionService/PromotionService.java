package com.example.pi.service.PromotionService;

/*import com.example.pi.entity.Command;*/

import com.example.pi.entity.Promotion;
import com.example.pi.entity.UserInfo;
import com.example.pi.repository.PromotionRepository.PromotionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.pi.repository.UserInfoRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private final UserInfoRepository userInfoRepository;
    /*@Autowired
    private CommandRepository commandRepository;*/


    private UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userInfoRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


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

   /* public Command applyPromotion(Long commandId, String promoCode) {
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
    }*/


    /*@Scheduled(cron = "0 0 9 * * ?")
    public void checkForAlmostExpiredPromotions() {
        LocalDate today = LocalDate.now();
        LocalDate alertDate = today.plusDays(2);

        List<Promotion> almostExpiredPromotions = promotionRepository.findAll().stream()
                .filter(promotion -> promotion.getExpiryDate() != null &&
                        promotion.getExpiryDate().isAfter(today) &&
                        promotion.getExpiryDate().isBefore(alertDate))
                .toList();

        for (Promotion promotion : almostExpiredPromotions) {
            System.out.println("ALERT: Promotion \"" + promotion.getTitle() + "\" is expiring soon on "
                    + promotion.getExpiryDate());

        }
    }*/



}