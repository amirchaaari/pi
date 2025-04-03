package com.example.pi.interfaces;

import com.example.pi.entity.Abonnement;
import java.util.List;

public interface IAbonnementService {
    Abonnement createAbonnement(Abonnement abonnement);
    Abonnement updateAbonnement(Long id, Abonnement abonnement);
    void deleteAbonnement(Long id);
    List<Abonnement> getAllAbonnements();
    Abonnement getAbonnementById(Long id);
}




