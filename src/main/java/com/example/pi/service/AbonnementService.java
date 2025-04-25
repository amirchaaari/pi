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
import org.springframework.data.crossstore.ChangeSetPersister;
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
        // Récupérer l'abonnement à partir de l'ID fourni
        Abonnement abonnement = abonnementRepository.findById(abonnementId)
                .orElseThrow(() -> new IllegalArgumentException("❌ Abonnement non trouvé."));

        // Vérifier que l'abonnement n'est pas déjà expiré
        if (abonnement.getEndDate().isAfter(newEndDate)) {
            throw new IllegalArgumentException("❌ La nouvelle date de fin doit être après l'ancienne.");
        }

        // ✅ On utilise directement l'utilisateur de l'abonnement
        UserInfo abonnementUser = abonnement.getUser();

        // Calcul des points gagnés pendant la durée de prolongation
        long days = ChronoUnit.DAYS.between(abonnement.getEndDate(), newEndDate);
        int gainedPoints = (int) days * 2;  // Exemple: chaque jour donne 2 points

        abonnementUser.setPoints(abonnementUser.getPoints() + gainedPoints);
        userInfoRepository.save(abonnementUser);

        // Mise à jour de l'abonnement avec les nouvelles dates
        abonnement.setStartDate(LocalDate.now());  // Date de début = aujourd'hui
        abonnement.setEndDate(newEndDate);
        abonnement.setStatus("active");
        abonnement.setEndDateOfRenewal(LocalDate.now());
        // Sauvegarder l’abonnement
        Abonnement updatedAbonnement = abonnementRepository.save(abonnement);

        // Mise à jour des trophées
        trophyService.updateUserTrophies(abonnementUser);

        return updatedAbonnement;
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
            interpretation = "🔥 Very good retention rate!";
        } else if (taux >= 40) {
            interpretation = "⚠️ Average retention rate.";
        } else {
            interpretation = "❌ Low retention rate, needs improvement.";
        }
        response.put("interpretation", interpretation);

        return response;
    }


    //@Scheduled(cron = "*/1 * * * * *")
    @Scheduled(cron = "0 0 0 * * ?")
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
