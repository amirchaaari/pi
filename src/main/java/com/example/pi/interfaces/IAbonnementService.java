package com.example.pi.interfaces;

import com.example.pi.entity.Abonnement;
import java.util.List;
import java.util.Map;

public interface IAbonnementService {
    // Méthode pour créer un abonnement après validation du Club Owner
    Abonnement updateAbonnement(Long id, Abonnement abonnement);
    void deleteAbonnement(Long id);
    List<Abonnement> getAllAbonnements();
    Abonnement getAbonnementById(Long id);
    double calculateRenewalRateForClub(Long clubId);
    Map<String, Object> analyzeClubPerformance(Long clubId);

}




