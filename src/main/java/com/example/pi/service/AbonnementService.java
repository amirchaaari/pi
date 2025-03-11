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
        Optional<Abonnement> existingAbonnement = abonnementRepository.findById(id);
        if (existingAbonnement.isPresent()) {
            Abonnement updatedAbonnement = existingAbonnement.get();
            updatedAbonnement.setStartDate(abonnement.getStartDate());
            updatedAbonnement.setStatus(abonnement.getStatus());
            return abonnementRepository.save(updatedAbonnement);
        }
        return null;
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
