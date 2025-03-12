package com.example.pi.service;

import com.example.pi.entity.Command;
import com.example.pi.repository.CommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommandService {

    @Autowired
    private CommandRepository commandRepository;

    public List<Command> findAll() {
        return commandRepository.findAll();
    }

    public Optional<Command> findById(Long id) {
        return commandRepository.findById(id);
    }

    public Command save(Command command) {
        return commandRepository.save(command);
    }

    public void deleteById(Long id) {
        commandRepository.deleteById(id);
    }
}