package com.example.pi.service;

import com.example.pi.entity.Pack;
import com.example.pi.interfaces.IPackService;
import com.example.pi.repository.AbonnementRepository;
import com.example.pi.repository.PackRepository;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import com.example.pi.entity.Club;
import com.example.pi.repository.ClubRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class PackService implements IPackService {


    private PackRepository packRepository;
    private ClubRepository clubRepository;
    private AbonnementRepository abonnementRepository;


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



    @Override
    public List<Pack> getPacksByPopularity() {
        List<Pack> packs = packRepository.findAll();

        // Récupérer les comptes d'abonnements
        List<Object[]> counts = abonnementRepository.countAbonnementsByPack();

        // Créer une map pour un accès rapide
        Map<Long, Integer> countMap = new HashMap<>();
        for (Object[] obj : counts) {
            Long packId = (Long) obj[0];
            Long count = (Long) obj[1];
            countMap.put(packId, count.intValue());
        }

        // Mettre à jour chaque pack avec le nombre de souscriptions
        for (Pack pack : packs) {
            int count = countMap.getOrDefault(pack.getId(), 0);
            pack.setSubscriptionCount(count);
        }

        // Trier les packs par popularité (croissant ou décroissant selon ton besoin)
        packs.sort(Comparator.comparingInt(Pack::getSubscriptionCount).reversed()); // décroissant

        return packs;
    }



}