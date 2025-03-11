package com.example.pi.service;

import com.example.pi.entity.Club;
import com.example.pi.interfaces.IClubService;
import com.example.pi.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;


import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClubService implements IClubService {

    @Autowired
    private ClubRepository clubRepository;

    @Override
    public Club createClub(Club club) {

        return clubRepository.save(club);
    }

    @Override
    public Club updateClub(Long id, Club club) {
        Optional<Club> existingClub = clubRepository.findById(id);
        if (existingClub.isPresent()) {
            Club updatedClub = existingClub.get();
            updatedClub.setName(club.getName());
            updatedClub.setDescription(club.getDescription());
            updatedClub.setCapacity(club.getCapacity());
            return clubRepository.save(updatedClub);
        }
        return null;
    }

    @Override
    public void deleteClub(Long id) {

        clubRepository.deleteById(id);
    }

    @Override
    public Club getClubById(Long id) {

        return clubRepository.findById(id).orElse(null);
    }

    @Override
    public List<Club> getAllClubs() {

        return clubRepository.findAll();
    }
}
