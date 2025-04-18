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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

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


        return abonnementRepository.findById(id).orElse(null);
    }

    @Override
    public List<Abonnement> getAllAbonnements() {
        List<Abonnement> abonnements = abonnementRepository.findAll();

        if (abonnements.isEmpty()) {
            System.out.println("Aucun abonnement trouvé");
        }

        return abonnements;
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
        if (!abonnement.getUser().equals(currentUser)) return null;

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
        abonnement.setEndDateOfRenewal(newEndDate);
        abonnement.setStatus("actif");

        // Mise à jour des trophées
        trophyService.updateUserTrophies(currentUser);

        return abonnementRepository.save(abonnement);
    }

    public double calculateRenewalRateForClub(Long clubId) {
        List<Abonnement> abonnements = abonnementRepository.findByPackClubId(clubId);

        if (abonnements.isEmpty()) return 0.0;

        long totalAbonnements = abonnements.size();
        long renouvellements = abonnements.stream()
                .filter(a -> a.getEndDateOfRenewal() != null)
                .count();

        return (double) renouvellements / totalAbonnements * 100;
    }

    @Override
    public Map<String, Object> analyzeClubPerformance(Long clubId) {
        Map<String, Object> response = new HashMap<>();

        // Popularité des packs
        List<Pack> packs = packRepository.findByClubId(clubId);
        List<Map<String, Object>> packPerformanceList = new ArrayList<>();

        if (packs.isEmpty()) {
            response.put("message", "❌ Aucun pack trouvé pour ce club.");
        } else {
            packs.forEach(pack -> {
                Map<String, Object> packDTO = new HashMap<>();
                packDTO.put("packName", pack.getName());
                packDTO.put("subscriptionCount", pack.getSubscriptionCount());
                packPerformanceList.add(packDTO);
            });
            response.put("packPerformance", packPerformanceList);
            response.put("message", "✅ Analyse de performance du club réussie.");
        }

        // Taux de renouvellement
        double taux = calculateRenewalRateForClub(clubId);
        response.put("renewalRate", taux);

        // Interprétation
        String interpretation;
        if (taux >= 70) {
            interpretation = "🔥 Très bon taux de fidélisation !";
        } else if (taux >= 40) {
            interpretation = "⚠️ Taux de fidélisation moyen.";
        } else {
            interpretation = "❌ Faible fidélisation, à améliorer.";
        }
        response.put("interpretation", interpretation);

        return response;
    }


     @Scheduled( cron = "0 */1 * * * ?")
    public void checkAndExpireAbonnements() {
         List<Abonnement> abonnements = abonnementRepository.findAll();
         LocalDate today = LocalDate.now();

         for (Abonnement abonnement : abonnements) {
             if (abonnement.getEndDate().isBefore(today)) {
                 abonnement.setStatus("expired");
                 abonnementRepository.save(abonnement);
             }else{
                    abonnement.setStatus("actif");
                    abonnementRepository.save(abonnement);
             }
         }
     }









}
