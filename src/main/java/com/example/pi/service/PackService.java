package com.example.pi.service;

import com.example.pi.entity.Pack;
import com.example.pi.interfaces.IPackService;
import com.example.pi.repository.PackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import com.example.pi.entity.Club;
import com.example.pi.repository.ClubRepository;
import java.util.HashSet;
import java.util.Set;



import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PackService implements IPackService {

    @Autowired
    private PackRepository packRepository;
    private ClubRepository clubRepository;

    @Override
    public Pack createPack(Pack pack) {
        return packRepository.save(pack);
    }

    @Override
    public Pack updatePack(Long id, Pack pack) {
        Optional<Pack> existingPack = packRepository.findById(id);
        if (existingPack.isPresent()) {
            Pack updatedPack = existingPack.get();
            updatedPack.setName(pack.getName());
            updatedPack.setPrice(pack.getPrice());
            updatedPack.setDuration(pack.getDuration());
            return packRepository.save(updatedPack);
        }
        return null;
    }

    @Override
    public void deletePack(Long id) {
        packRepository.deleteById(id);
    }

    @Override
    public Pack getPackById(Long id) {
        return packRepository.findById(id).orElse(null);
    }

    @Override
    public List<Pack> getAllPacks() {
        return packRepository.findAll();
    }

    @Override
    public Club affecterPackToClub(Long clubId, Long packId) {
        // Récupérer le pack avec une gestion d'exception si le pack n'est pas trouvé
        Pack pack = packRepository.findById(packId)
                .orElseThrow(() -> new RuntimeException("Pack non trouvé"));

        // Récupérer le club avec une gestion d'exception si le club n'est pas trouvé
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club non trouvé"));

        // Initialiser un Set vide si le club n'a pas encore de packs
        Set<Pack> packsMisesAJour = Optional.ofNullable(club.getPacks()).orElse(new HashSet<>());

        // Ajouter le pack au Set
        packsMisesAJour.add(pack);

        // Mettre à jour la liste des packs du club
        club.setPacks(packsMisesAJour);

        // Associer le club au pack
        pack.setClub(club);

        // Sauvegarder le pack pour s'assurer que la relation est bien mise à jour
        packRepository.save(pack);

        // Sauvegarder le club avec la liste mise à jour
        return clubRepository.save(club);
    }

}
