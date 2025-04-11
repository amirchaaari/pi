package com.example.pi.controller;

import com.example.pi.entity.Command;
import com.example.pi.service.CommandService;
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
        return commandService.findAll();
    }

    @PostMapping

    public ResponseEntity<?> createCommand(@RequestBody Command command) {
        try {
            Command createdCommand = commandService.save(command);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCommand);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommand(@PathVariable Long id) {
        commandService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}