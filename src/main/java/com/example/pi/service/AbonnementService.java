package com.example.pi.service;

import com.example.pi.entity.Abonnement;
import com.example.pi.entity.ClubOwner;
import com.example.pi.entity.Pack;
import com.example.pi.entity.UserInfo;
import com.example.pi.interfaces.IAbonnementService;
import com.example.pi.repository.AbonnementRepository;
import com.example.pi.repository.PackRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AbonnementService implements IAbonnementService {

    private final AbonnementRepository abonnementRepository;
    private final PackRepository packRepository;

    // Méthode utilitaire pour récupérer l'utilisateur authentifié
    private UserInfo getCurrentUser() {
        return (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // Méthode pour créer un abonnement après validation du Club Owner
    @Override
    public Abonnement createAbonnement(Long packId) {
        UserInfo currentUser = getCurrentUser();

        if (currentUser instanceof ClubOwner) {
            // Le Club Owner crée l'abonnement pour un utilisateur
            Pack pack = packRepository.findById(packId)
                    .orElseThrow(() -> new IllegalArgumentException("Pack non trouvé"));

            Abonnement abonnement = new Abonnement();
            abonnement.setPack(pack);
            abonnement.setUser(currentUser);  // Associer l'abonnement à l'utilisateur
            abonnement.setStartDate(LocalDate.now());  // Par exemple
            abonnement.setEndDate(LocalDate.now().plusMonths(pack.getDuration()));
            abonnement.setStatus("actif");

            return abonnementRepository.save(abonnement);
        } else {
            throw new IllegalArgumentException("Seul un Club Owner peut créer un abonnement");
        }
    }

    // Méthode pour mettre à jour le statut de l'abonnement par un administrateur
    public void updateStatusByAdmin(Long id, String newStatus) {
        Optional<Abonnement> abonnementOpt = abonnementRepository.findById(id);
        if (abonnementOpt.isPresent()) {
            Abonnement abonnement = abonnementOpt.get();

            // Vérifier si l'utilisateur authentifié est un administrateur
            UserInfo currentUser = getCurrentUser();
            if (currentUser.getRoles() != null && currentUser.getRoles().contains("ADMIN")) {
                abonnement.setStatus(newStatus);
                abonnementRepository.save(abonnement);
            } else {
                throw new IllegalArgumentException("Seul un administrateur peut modifier l'état de cet abonnement");
            }
        } else {
            throw new IllegalArgumentException("Abonnement non trouvé");
        }
    }

    @Override
    public Abonnement updateAbonnement(Long id, Abonnement abonnement) {
        // Implémenter la mise à jour de l'abonnement si nécessaire
        return null;
    }

    // Méthode pour supprimer un abonnement
    @Override
    public void deleteAbonnement(Long id) {
        Optional<Abonnement> abonnementOpt = abonnementRepository.findById(id);
        if (abonnementOpt.isPresent()) {
            Abonnement abonnement = abonnementOpt.get();
            UserInfo currentUser = getCurrentUser();

            // Vérifier que l'abonnement appartient à l'utilisateur authentifié ou au Club Owner
            if (abonnement.getUser().getId()==(currentUser.getId()) ||
                    (currentUser instanceof ClubOwner && abonnement.getPack().getClub().getOwner().getId()==(currentUser.getId()))) {
                abonnementRepository.deleteById(id);
            } else {
                throw new IllegalArgumentException("Vous n'avez pas les droits pour supprimer cet abonnement");
            }
        } else {
            throw new IllegalArgumentException("Abonnement non trouvé");
        }
    }

    // Méthode pour récupérer un abonnement par ID
    @Override
    public Abonnement getAbonnementById(Long id) {
        Optional<Abonnement> abonnementOpt = abonnementRepository.findById(id);
        if (abonnementOpt.isPresent()) {
            Abonnement abonnement = abonnementOpt.get();
            UserInfo currentUser = getCurrentUser();

            // Vérifier que l'abonnement appartient à l'utilisateur authentifié
            if (abonnement.getUser().getId()==(currentUser.getId()) ||
                    (currentUser instanceof ClubOwner && abonnement.getPack().getClub().getOwner().getId()==(currentUser.getId()))) {
                return abonnement;
            } else {
                throw new IllegalArgumentException("Vous n'avez pas accès à cet abonnement");
            }
        }
        return null;
    }

    // Méthode pour récupérer tous les abonnements d'un utilisateur
    @Override
    public List<Abonnement> getAllAbonnements() {
        UserInfo currentUser = getCurrentUser();
        return abonnementRepository.findByUserId(currentUser.getId());
    }
}
