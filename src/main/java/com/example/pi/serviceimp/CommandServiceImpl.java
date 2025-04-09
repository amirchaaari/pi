package com.example.pi.serviceimp;
import com.example.pi.repository.UserInfoRepository;
import com.example.pi.entity.Command;
import com.example.pi.entity.Product;
import com.example.pi.entity.UserInfo;
import com.example.pi.repository.CommandRepository;
import com.example.pi.repository.ProductRepository;
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

    @Transactional
    public Command createCommand(Long productId, Integer quantity) {
        UserInfo user = getCurrentUser(); // Get the current user
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Invalid product"));

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        // Create the command
        Command command = new Command();
        command.setUser(user);
        command.setProduct(product); // Ensure product is set
        command.setQuantity(quantity);

        return commandRepository.save(command); // Save the command
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
    public Command updateCommand(Long id, Command commandDetails) {
        Command command = commandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Command not found with id: " + id));
        command.setProduct(commandDetails.getProduct());
        command.setQuantity(commandDetails.getQuantity());
        command.setUser(commandDetails.getUser());
        return commandRepository.save(command);
    }

    @Transactional
    public void deleteById(Long id) {
        Command command = commandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Command not found with id: " + id));
        commandRepository.delete(command);
    }
}