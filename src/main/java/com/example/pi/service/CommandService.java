package com.example.pi.service;

import com.example.pi.entity.Command;

import java.util.List;

public interface CommandService {
    Command createCommand(Long productId, Integer quantity); // Create a new command

    List<Command> getCommandsByUser(Long userId);
    List<Command> getCommandsByUser();
    Command getCommandById(Long id); // Retrieve a command by ID
    List<Command> getAllCommands(); // Get all commands
    Command updateCommand(Long id, Command commandDetails); // Update an existing command
    void deleteById(Long id); // Delete a command by ID

}