package com.example.pi.service;

import com.example.pi.entity.Abonnement;
import com.example.pi.interfaces.IAbonnementService;
import com.example.pi.repository.AbonnementRepository;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;


import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AbonnementService implements IAbonnementService {

    private AbonnementRepository abonnementRepository;

    @Override
    public Abonnement createAbonnement(Abonnement abonnement) {

        return abonnementRepository.save(abonnement);
    }

    @Override
    public Abonnement updateAbonnement(Long id, Abonnement abonnement) {
        Optional<Abonnement> existingAbonnementOpt = abonnementRepository.findById(id);
        if (existingAbonnementOpt.isPresent()) {
            Abonnement existingAbonnement = existingAbonnementOpt.get();

            // Mettre à jour les champs de l'abonnement
            existingAbonnement.setStartDate(abonnement.getStartDate());
            existingAbonnement.setEndDate(abonnement.getEndDate());  // Ajout de la mise à jour de la date de fin
            existingAbonnement.setStatus(abonnement.getStatus());
            existingAbonnement.setPack(abonnement.getPack());  // Mise à jour du pack
            existingAbonnement.setGymGoer(abonnement.getGymGoer());  // Mise à jour du GymGoer

            // Sauvegarder et retourner l'abonnement mis à jour
            return abonnementRepository.save(existingAbonnement);
        }
        return null;  // Si l'abonnement n'existe pas
    }

    @Override
    public void deleteAbonnement(Long id) {

        abonnementRepository.deleteById(id);
    }

    @Override
    public Abonnement getAbonnementById(Long id) {

        return abonnementRepository.findById(id).orElse(null);
    }

    @Override
    public List<Abonnement> getAllAbonnements() {

        return abonnementRepository.findAll();
    }
}
