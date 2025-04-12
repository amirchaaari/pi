package com.example.pi.service;

import com.example.pi.entity.Abonnement;
import com.example.pi.entity.Pack;
import com.example.pi.entity.UserInfo;
import com.example.pi.interfaces.IAbonnementService;
import com.example.pi.repository.AbonnementRepository;
import com.example.pi.repository.PackRepository;
import com.example.pi.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AbonnementService implements IAbonnementService {

    private final AbonnementRepository abonnementRepository;
    private final PackRepository packRepository;
    private final UserInfoRepository userInfoRepository;

    // Méthode pour récupérer l'utilisateur connecté
    private UserInfo getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userInfoRepository.findByEmail(username).orElse(null);
    }


    // Le Club Owner peut modifier les informations d’un abonnement
    @Override
    public Abonnement updateAbonnement(Long id, Abonnement newAbonnement) {
        Optional<Abonnement> optional = abonnementRepository.findById(id);
        if (optional.isEmpty()) return null;

        Abonnement old = optional.get();
        UserInfo currentUser = getCurrentUser();
        if (currentUser == null) return null;

        int ownerId = old.getPack().getClub().getOwner().getId();
        if (currentUser.getId() == ownerId) {
            old.setStartDate(newAbonnement.getStartDate());
            old.setEndDate(newAbonnement.getEndDate());
            old.setStatus(newAbonnement.getStatus());
            return abonnementRepository.save(old);
        }

        return null;
    }

    // Un utilisateur ou Club Owner peut supprimer l'abonnement
    @Override
    public void deleteAbonnement(Long id) {
        Optional<Abonnement> optional = abonnementRepository.findById(id);
        if (optional.isEmpty()) return;

        Abonnement abonnement = optional.get();
        UserInfo currentUser = getCurrentUser();
        if (currentUser == null) return;

        int userId = currentUser.getId();
        int ownerId = abonnement.getPack().getClub().getOwner().getId();

        if (abonnement.getUser().getId() == userId || userId == ownerId) {
            abonnementRepository.deleteById(id);
        }
    }

    // Accès à un abonnement : utilisateur concerné ou owner du club
    @Override
    public Abonnement getAbonnementById(Long id) {
        Abonnement abonnement = updateAbonnementStatusIfExpired(id); // vérification automatique du statut
        if (abonnement == null) return null;

        UserInfo currentUser = getCurrentUser();
        if (currentUser == null) return null;

        int userId = currentUser.getId();
        int ownerId = abonnement.getPack().getClub().getOwner().getId();

        if (abonnement.getUser().getId() == userId || userId == ownerId) {
            return abonnement;
        }

        return null;
    }


    // Liste des abonnements de l’utilisateur connecté
    @Override
    public List<Abonnement> getAllAbonnements() {
        UserInfo currentUser = getCurrentUser();
        if (currentUser == null) return Collections.emptyList();

        List<Abonnement> abonnements = abonnementRepository.findByUserId(currentUser.getId());

        abonnements.forEach(abonnement -> {
            if (abonnement.getEndDate().isBefore(LocalDate.now()) && !"expiré".equalsIgnoreCase(abonnement.getStatus())) {
                abonnement.setStatus("expiré");
                abonnementRepository.save(abonnement);
            }
        });

        return abonnementRepository.findAll();
    }




    // Le Club Owner voit toutes les demandes d’abonnements sur ses packs
    public List<Abonnement> getAbonnementsForClubOwner() {
        UserInfo currentUser = getCurrentUser();
        if (currentUser == null) return Collections.emptyList();
        return abonnementRepository.findByPack_Club_Owner_Id(currentUser.getId());
    }

    public Abonnement updateAbonnementStatusIfExpired(Long abonnementId) {
        Optional<Abonnement> optional = abonnementRepository.findById(abonnementId);
        if (optional.isEmpty()) return null;

        Abonnement abonnement = optional.get();
        LocalDate today = LocalDate.now();

        if (abonnement.getEndDate().isBefore(today)) {
            abonnement.setStatus("expiré");
        }

        return abonnementRepository.save(abonnement);
    }

}
