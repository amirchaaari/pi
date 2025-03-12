package com.example.pi.controller;

import com.example.pi.entity.Command;
import com.example.pi.service.CommandService; // Ensure correct import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commands")
public class CommandController {

    @Autowired
    private CommandService commandService;

    @GetMapping
    public List<Command> getAllCommands() {
        return commandService.getAllCommands(); // Retrieve all commands
    }

    @GetMapping("/{id}")
    public ResponseEntity<Command> getCommandById(@PathVariable Long id) {
        Command command = commandService.getCommandById(id);
        if (command != null) {
            return ResponseEntity.ok(command); // Return command if found
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }

    @PostMapping
    public ResponseEntity<Command> createCommand(@RequestBody Command command) {
        Command createdCommand = commandService.createCommand(command); // Save new command
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommand); // Return 201 status
    }

    @PutMapping("/{id}")
    public ResponseEntity<Command> updateCommand(@PathVariable Long id, @RequestBody Command commandDetails) {
        Command updatedCommand = commandService.updateCommand(id, commandDetails);
        if (updatedCommand != null) {
            return ResponseEntity.ok(updatedCommand); // Return updated command
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommand(@PathVariable Long id) {
        commandService.deleteById(id); // Delete command by ID
        return ResponseEntity.noContent().build(); // Return 204 status
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // Handle exceptions
    }
}