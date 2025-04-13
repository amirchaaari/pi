package com.example.pi.service;

import com.example.pi.entity.Abonnement;
import com.example.pi.entity.Pack;
import com.example.pi.entity.UserInfo;
import com.example.pi.interfaces.IAbonnementService;
import com.example.pi.repository.AbonnementRepository;
import com.example.pi.repository.PackRepository;
import com.example.pi.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AbonnementService implements IAbonnementService {

    @Autowired
    private final AbonnementRepository abonnementRepository;
    private final PackRepository packRepository;
    private final UserInfoRepository userInfoRepository;
    private TrophyService trophyService;

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

    @Override
    public Abonnement getAbonnementById(Long id) {
        LocalDate newEndDate = LocalDate.now();

        Abonnement abonnement = updateAbonnementStatusIfExpired(id, newEndDate);
        if (abonnement == null) return null;

        return abonnement;
    }

    @Override
    public List<Abonnement> getAllAbonnements() {
        List<Abonnement> abonnements = abonnementRepository.findAll();

        if (abonnements.isEmpty()) {
            System.out.println("Aucun abonnement trouvé");
        }

        abonnements.forEach(abonnement -> {
            System.out.println("Mise à jour du statut de l'abonnement " + abonnement.getId());
            updateAbonnementStatusIfExpired(abonnement.getId(), LocalDate.now()); // Met à jour le statut
        });

        return abonnements; // Retourne tous les abonnements après avoir mis à jour leur statut
    }





    public Abonnement updateAbonnementStatusIfExpired(Long abonnementId, LocalDate newEndDate) {
        Optional<Abonnement> optional = abonnementRepository.findById(abonnementId);
        if (optional.isEmpty()) return null;

        Abonnement abonnement = optional.get();
        LocalDate today = LocalDate.now();

        // Vérifier si la date de fin est dépassée
        if (abonnement.getEndDate().isBefore(today)) {

            // Vérifier si la date de fin de renouvellement est null ou dépassée
            if (abonnement.getEndDateOfRenewal() == null || abonnement.getEndDateOfRenewal().isBefore(today)) {
                abonnement.setStatus("expiré"); // Mettre le statut à "expiré"
            }

            // Si la date de fin de renouvellement est fournie, mettre à jour l'abonnement
            if (newEndDate != null) {
                abonnement.setEndDate(newEndDate); // Mettre à jour la date de fin de l'abonnement
                abonnement.setEndDateOfRenewal(newEndDate); // Mettre à jour la date de fin de renouvellement si nécessaire
            }
        }

        return abonnementRepository.save(abonnement);
    }

    public List<Abonnement> getUserAbonnementsHistory() {
        UserInfo currentUser = getCurrentUser();
        if (currentUser == null) return Collections.emptyList();  // Si l'utilisateur n'est pas trouvé, renvoyer une liste vide

        // Récupérer tous les abonnements associés à cet utilisateur
        return abonnementRepository.findByUserId(currentUser.getId());
    }


    public Abonnement renewAbonnement(Long abonnementId, LocalDate newEndDate) {
        Optional<Abonnement> optional = abonnementRepository.findById(abonnementId);
        if (optional.isEmpty()) return null;

        Abonnement abonnement = optional.get();
        UserInfo currentUser = getCurrentUser();

        // Vérifie que l'utilisateur courant est bien le propriétaire de l'abonnement
        if (currentUser == null || !abonnement.getUser().equals(currentUser)) return null;

        LocalDate currentEndDate = abonnement.getEndDate();

        // Empêche un renouvellement vers une date antérieure
        if (newEndDate.isBefore(currentEndDate)) {
            throw new IllegalArgumentException("La nouvelle date doit être après la date de fin actuelle.");
        }

        // Calcul des points gagnés
        long days = ChronoUnit.DAYS.between(currentEndDate, newEndDate);
        int gainedPoints = (int) days * 2;

        // Mise à jour des points de l'utilisateur
        currentUser.setPoints(currentUser.getPoints() + gainedPoints);
        userInfoRepository.save(currentUser);

        // Mise à jour de l'abonnement
        abonnement.setEndDate(newEndDate);
        abonnement.setEndDateOfRenewal(newEndDate); // tu peux supprimer cette ligne si elle est redondante
        abonnement.setStatus("actif");

        // Mise à jour des trophées
        trophyService.updateUserTrophies(currentUser);

        return abonnementRepository.save(abonnement);
    }



}
