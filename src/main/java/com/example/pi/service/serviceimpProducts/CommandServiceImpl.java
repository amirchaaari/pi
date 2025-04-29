package com.example.pi.service.serviceimpProducts;

import com.example.pi.entity.Command;
import com.example.pi.entity.Livraison;
import com.example.pi.entity.Product;
import com.example.pi.entity.UserInfo;
import com.example.pi.repository.CommandRepository;
import com.example.pi.repository.LivraisonRepository.LivraisonRepository;
import com.example.pi.repository.LivraisonRepository.LivreurRepository;
import com.example.pi.repository.ProductRepository;
import com.example.pi.repository.UserInfoRepository;
import com.example.pi.service.CommandService;
import com.example.pi.service.EmailService;
import com.example.pi.service.LivraisonService.LivraisonService;
import com.example.pi.service.Maps.MapsService;
import com.google.maps.errors.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
@Slf4j
@RequiredArgsConstructor
@Service
public class CommandServiceImpl implements CommandService {

    private final CommandRepository commandRepository;
    private final UserInfoRepository userInfoRepository;//
    private final ProductRepository productRepository;
    private final LivreurRepository livreurRepository;
    private final EmailService emailService;
    private final MapsService mapsService;

    private final LivraisonRepository livraisonRepository;
    private final LivraisonService livraisonService;
    private UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        return userInfoRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

//    @Override
//    @Transactional
//    public Command createCommand(Long productId, Integer quantity) {
//        UserInfo user = getCurrentUser();
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
//
//        Command command = new Command();
//        command.setUser(user);
//        command.setProduct(product);
//        command.setQuantity(quantity);
//
//        return commandRepository.save(command);
//    }


    @Transactional
    @Override
    public Command createCommand(Long productId, Integer quantity) {
        UserInfo user = getCurrentUser();
        Product product = null;
        try {
            product = productRepository.findById(productId)
                    .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }

        // Validate quantity
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

         Command command = new Command();
           command.setUser(user);
            command.setProduct(product);
             command.setQuantity(quantity);
            commandRepository.save(command);
            Livraison livraison = Livraison.builder()
                .address(user.getAddress())
                .status(Livraison.DeliveryStatus.PENDING)
                .scheduleddate(calculateDeliveryDate(3))
                .build();
        livraisonService.addLivraison(livraison);





//        livraison.setCommand(command);
//        livraisonRepository.save(livraison);
//        command.setLivraison(livraison);
//        commandRepository.save(command);




        log.info("Created command {} for user {}", command.getId(), user.getEmail());
        return command;

  }

    private java.util.Date calculateDeliveryDate(int daysToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
        return calendar.getTime();
    }
        @Transactional
    public List<Command> getCommandsByUser() {
        UserInfo user = getCurrentUser();
        return commandRepository.findByUserId((long) user.getId());
    }

    @Transactional
    public Command getCommandById(Long id) {
        return commandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Command not found"));
    }

    @Transactional
    public List<Command> getAllCommands() {
        return commandRepository.findAll();
    }

    @Transactional
    public Command updateCommand(Long id, Integer quantity, Long productId) {
        Command command = commandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Command not found"));

        if (quantity != null) {
            command.setQuantity(quantity);
        }

        if (productId != null) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));
            command.setProduct(product);
        }

        return commandRepository.save(command);
    }

    @Transactional
    public void deleteById(Long id) {
        Command command = commandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Command not found with id: " + id));
        commandRepository.delete(command);
    }

    @Transactional
    public List<Command> getCommandsByUserWithProducts() {
        UserInfo user = getCurrentUser();
        return commandRepository.findCommandsWithProductsByUserId((long) user.getId());
    }
}