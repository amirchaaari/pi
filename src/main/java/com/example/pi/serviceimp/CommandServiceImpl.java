package com.example.pi.serviceimp;

import com.example.pi.entity.Command;
import com.example.pi.repository.CommandRepository;
import com.example.pi.service.CommandService; // Ensure correct import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommandServiceImpl implements CommandService {

    @Autowired
    private CommandRepository commandRepository;

    @Override
    public Command createCommand(Command command) {
        return commandRepository.save(command); // Save the new command
    }

    @Override
    public Command getCommandById(Long id) {
        Optional<Command> command = commandRepository.findById(id);
        return command.orElse(null); // Handle case where command is not found
    }

    @Override
    public List<Command> getAllCommands() {
        return commandRepository.findAll(); // Retrieve all commands
    }

    @Override
    public Command updateCommand(Long id, Command commandDetails) {
        if (commandRepository.existsById(id)) {
            commandDetails.setId(id); // Set the ID to ensure correct update
            return commandRepository.save(commandDetails);
        }
        return null; // Or throw an exception if needed
    }

    @Override
    public void deleteById(Long id) {
        commandRepository.deleteById(id); // Delete the command by ID
    }
}