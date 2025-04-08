package com.example.pi.service;

import com.example.pi.entity.Abonnement;
import com.example.pi.entity.Pack;
import com.example.pi.entity.UserInfo;
import com.example.pi.interfaces.IAbonnementService;
import com.example.pi.repository.AbonnementRepository;
import com.example.pi.repository.PackRepository;
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

    // Méthode pour récupérer l'utilisateur connecté
    private UserInfo getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (principal instanceof UserInfo) ? (UserInfo) principal : null;
    }

    // Un utilisateur fait une demande d’abonnement (statut par défaut : en_attente)
    @Override
    public Abonnement createAbonnement(Long packId) {
        UserInfo currentUser = getCurrentUser();
        if (currentUser == null) return null;

        Optional<Pack> optionalPack = packRepository.findById(packId);
        if (optionalPack.isEmpty()) return null;

        Pack pack = optionalPack.get();
        int duration = (pack.getDuration() > 0) ? pack.getDuration() : 1;

        Abonnement abonnement = new Abonnement();
        abonnement.setPack(pack);
        abonnement.setUser(currentUser);
        abonnement.setStartDate(LocalDate.now());
        abonnement.setEndDate(LocalDate.now().plusMonths(duration));
        abonnement.setStatus("en_attente");

        return abonnementRepository.save(abonnement);
    }

    // Le Club Owner valide la demande d'abonnement
    public Abonnement validerAbonnement(Long abonnementId) {
        Optional<Abonnement> optional = abonnementRepository.findById(abonnementId);
        if (optional.isEmpty()) return null;

        Abonnement abonnement = optional.get();
        UserInfo currentUser = getCurrentUser();
        if (currentUser == null) return null;

        int ownerId = abonnement.getPack().getClub().getOwner().getId();
        if (currentUser.getId() == ownerId) {
            abonnement.setStatus("actif");
            return abonnementRepository.save(abonnement);
        }

        return null;
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
        Optional<Abonnement> optional = abonnementRepository.findById(id);
        if (optional.isEmpty()) return null;

        Abonnement abonnement = optional.get();
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
        return abonnementRepository.findByUserId(currentUser.getId());
    }

    // Le Club Owner voit toutes les demandes d’abonnements sur ses packs
    public List<Abonnement> getAbonnementsForClubOwner() {
        UserInfo currentUser = getCurrentUser();
        if (currentUser == null) return Collections.emptyList();
        return abonnementRepository.findByPack_Club_Owner_Id(currentUser.getId());
    }
}
