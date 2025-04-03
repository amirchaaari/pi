package com.example.pi.service;

import com.example.pi.entity.Club;
import com.example.pi.entity.Sport;
import com.example.pi.interfaces.IClubService;
import com.example.pi.repository.ClubRepository;
import com.example.pi.repository.SportRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class ClubService implements IClubService {

    private final ClubRepository clubRepository;
    private final SportRepository sportRepository;

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

    @Override
    public Club affecterSportToClub(Long clubId, Long sportId) {
        // Récupérer le sport avec une gestion d'exception si le sport n'est pas trouvé
        Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new RuntimeException("Sport non trouvé"));

        // Récupérer le club avec une gestion d'exception si le club n'est pas trouvé
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club non trouvé"));

        // Initialiser un Set vide si le club n'a pas encore de sports
        Set<Sport> sportsMisesAJour = Optional.ofNullable(club.getSports()).orElse(new HashSet<>());

        // Ajouter le sport au Set
        sportsMisesAJour.add(sport);

        // Mettre à jour la liste des sports du club
        club.setSports(sportsMisesAJour);

        // Sauvegarder le club avec la liste mise à jour
        return clubRepository.save(club);
    }
}
