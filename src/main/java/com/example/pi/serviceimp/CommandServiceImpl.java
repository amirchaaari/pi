package com.example.pi.serviceimp;

import com.example.pi.entity.Command;
import com.example.pi.entity.Livraison;
import com.example.pi.entity.Product;
import com.example.pi.entity.UserInfo;
import com.example.pi.repository.CommandRepository;
import com.example.pi.repository.LivraisonRepository;
import com.example.pi.repository.ProductRepository;
import com.example.pi.repository.UserInfoRepository;
import com.example.pi.service.CommandService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommandServiceImpl implements CommandService {

    @Autowired
    private final CommandRepository commandRepository;

    @Autowired
    private final UserInfoRepository userInfoRepository;

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final LivraisonRepository livraisonRepository;

    private UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userInfoRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

//    @Override
//    @Transactional
//    public Command createCommand(Long productId, Integer quantity) {
//        UserInfo user = getCurrentUser(); // Get the current user instead of passing userId
//
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
    public List<Command> getCommandsByUser() {
        UserInfo user = getCurrentUser(); // Get the current user
        return commandRepository.findByUserId((long) user.getId()); // Assuming you have this method in your repository
    }

    @Transactional
    public Command getCommandById(Long id) {
        Optional<Command> command = commandRepository.findById(id);
        return command.orElse(null); // Handle case where command is not found
    }

    @Transactional
    public List<Command> getAllCommands() {
        return commandRepository.findAll(); // Retrieve all commands
    }

    @Transactional
    public Command updateCommand(Long id, Integer quantity, Long productId) {
        Command command = commandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Command not found"));

        // Only update what's needed
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
        UserInfo user = getCurrentUser(); // Get the current user
        return commandRepository.findCommandsWithProductsByUserId((long) user.getId());
    }



    @Override
    @Transactional
    public Command createCommand(Long productId, Integer quantity) {
        // Get the current user instead of passing userId
        UserInfo user = getCurrentUser();

        // Find the product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        // Create and configure the delivery
        Livraison livraison = new Livraison();
        livraison.setAddress(user.getAddress()); // Assuming UserInfo has an address field
        livraison.setStatus(Livraison.DeliveryStatus.PENDING);

        // Set scheduled date to today + 3 days
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 3);
        livraison.setScheduleddate(calendar.getTime());

        // Create the command
        Command command = new Command();
        command.setUser(user);
        command.setProduct(product);
        command.setQuantity(quantity);
        command.setLivraison(livraison);

        // Save the delivery first (since it's the owning side of the relationship)
        livraison = livraisonRepository.save(livraison);

        // Then save the command
        command = commandRepository.save(command);

        // Add the command to the delivery's set of commands
        livraison.setCommand(command);
        livraisonRepository.save(livraison);

        return command;
    }
}