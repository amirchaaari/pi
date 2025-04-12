package com.example.pi.service;

import com.example.pi.entity.Club;
import com.example.pi.entity.Sport;
import com.example.pi.interfaces.ISportService;
import com.example.pi.repository.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;


import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SportService implements ISportService {

    @Autowired
    private SportRepository sportRepository;

    @Override
    public Sport createSport(Sport sport) {
        return sportRepository.save(sport);
    }

    @Override
    public Sport updateSport(Long id, Sport sport) {
        Optional<Sport> existingSport = sportRepository.findById(id);
        if (existingSport.isPresent()) {
            Sport updatedSport = existingSport.get();
            updatedSport.setName(sport.getName());
            updatedSport.setDescription(sport.getDescription());
            return sportRepository.save(updatedSport);
        }
        return null;
    }

    @Override
    public void deleteSport(Long id) {
        Optional<Sport> sportOpt = sportRepository.findById(id);
        if (sportOpt.isEmpty()) {
            throw new RuntimeException("Sport not found with id: " + id);
        }

        Sport sport = sportOpt.get();

        // 🔄 Dissocier les clubs avant suppression
        for (Club club : sport.getClubs()) {
            club.getSports().remove(sport);
        }
        sport.getClubs().clear();

        sportRepository.delete(sport);
    }


    @Override
    public Sport getSportById(Long id) {
        return sportRepository.findById(id).orElse(null);
    }

    @Override
    public List<Sport> getAllSports() {
        return sportRepository.findAll();
    }
}
