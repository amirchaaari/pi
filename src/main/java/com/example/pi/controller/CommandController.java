package com.example.pi.controller;

import com.example.pi.entity.AddToCartRequest;
import com.example.pi.entity.Command;
import com.example.pi.service.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/commands")
public class CommandController {

    @Autowired
    private CommandService commandService;

    @GetMapping
//    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public List<Command> getAllCommands() {
        return commandService.getAllCommands(); // Retrieve all commands
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Command> getCommandById(@PathVariable Long id) {
        Command command = commandService.getCommandById(id);
        if (command != null) {
            return ResponseEntity.ok(command); // Return command if found
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }

    @PostMapping
//    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Command> createCommand(@RequestBody AddToCartRequest request) {
        Command createdCommand = commandService.createCommand(request.getProductId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommand);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Command> updateCommand(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        // Extract parameters from the request body
        Integer quantity = (Integer) updates.get("quantity");
        Long productId = updates.get("productId") != null ? ((Number) updates.get("productId")).longValue() : null;

        Command updatedCommand = commandService.updateCommand(id, quantity, productId);
        return ResponseEntity.ok(updatedCommand);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> deleteCommand(@PathVariable Long id) {
        commandService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // Handle exceptions
    }

    @GetMapping("/user")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Map<String, Object>>> getCommandsWithProducts() {
        List<Command> commands = commandService.getCommandsByUserWithProducts(); // No userId needed

        List<Map<String, Object>> response = commands.stream()
                .map(command -> {
                    Map<String, Object> dto = new HashMap<>();
                    dto.put("id", command.getId());
                    dto.put("quantity", command.getQuantity());

                    if (command.getProduct() != null) {
                        Map<String, Object> product = new HashMap<>();
                        product.put("id", command.getProduct().getId());
                        product.put("name", command.getProduct().getName());
                        product.put("price", command.getProduct().getPrice());
                        product.put("imageUrl", command.getProduct().getImageUrl());
                        dto.put("product", product);
                    }

                    return dto;
                })
                .toList(); // Make sure Collectors is imported

        return ResponseEntity.ok(response);
    }
}