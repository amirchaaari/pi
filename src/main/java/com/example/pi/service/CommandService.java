package com.example.pi.service;

import com.example.pi.entity.Command;
import com.example.pi.entity.Livraison;
import com.example.pi.repository.CommandRepository;
import com.example.pi.repository.LivraisonRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommandService {

    @Autowired
    private CommandRepository commandRepository;

    private LivraisonRepository livraisonRepository;
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
    /*public Command addCommand(Command command) {

        Livraison livraison = new Livraison();
        livraison.setAddress(command.getUser().getAddress());


        LocalDateTime futureDate = LocalDateTime.now().plusDays(3);
        Date scheduledDate = Date.from(futureDate.atZone(ZoneId.systemDefault()).toInstant());
        livraison.setScheduleddate(scheduledDate);

        livraison.setStatus(Livraison.DeliveryStatus.PENDING);


        livraison = livraisonRepository.save(livraison);


        command.setLivraison(livraison);


        return commandRepository.save(command);
    }
*/




}