package com.example.pi.service.serviceimpProducts;

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

    private final CommandRepository commandRepository;
    private final UserInfoRepository userInfoRepository;
    private final ProductRepository productRepository;

    private UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        return userInfoRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional
    public Command createCommand(Long productId, Integer quantity) {
        UserInfo user = getCurrentUser();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        Command command = new Command();
        command.setUser(user);
        command.setProduct(product);
        command.setQuantity(quantity);

        return commandRepository.save(command);
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