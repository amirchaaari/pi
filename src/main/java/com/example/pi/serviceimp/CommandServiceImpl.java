package com.example.pi.serviceimp;

import com.example.pi.entity.Command;
import com.example.pi.entity.Product;
import com.example.pi.entity.UserInfo;
import com.example.pi.repository.CommandRepository;
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

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class CommandServiceImpl implements CommandService {

    @Autowired
    private CommandRepository commandRepository;


    @Autowired
    private final UserInfoRepository userInfoRepository;
    @Autowired
    private ProductRepository productRepository;

    private UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userInfoRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional
    public Command createCommand(Long productId, Integer quantity, Integer userId) {
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        Command command = new Command();
        command.setUser(user);
        command.setProduct(product);
        command.setQuantity(quantity);

        return commandRepository.save(command);
    }



    @Transactional
    public List<Command> getCommandsByUser(Long userId) {
        return commandRepository.findByUserId(userId);
    }

    @Override
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
    public Command updateCommand(Long id, Integer quantity, Long productId, Integer userId) {
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

        if (userId != null) {
            UserInfo user = userInfoRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            command.setUser(user);
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
    public List<Command> getCommandsByUserWithProducts(Long userId) {
        return commandRepository.findCommandsWithProductsByUserId(userId);
    }



}