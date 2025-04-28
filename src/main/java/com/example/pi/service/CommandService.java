package com.example.pi.service;

import com.example.pi.entity.Command;

import java.util.List;

public interface CommandService {
    Command createCommand(Long productId, Integer quantity); // Remove userId

    List<Command> getCommandsByUser(); // Remove userId
    Command getCommandById(Long id); // Retrieve a command by ID
    List<Command> getAllCommands(); // Get all commands
    Command updateCommand(Long id, Integer quantity, Long productId); // Remove userId
    void deleteById(Long id); // Delete a command by ID

    List<Command> getCommandsByUserWithProducts(); // Remove userId
}