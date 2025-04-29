package com.example.pi.service;

import com.example.pi.entity.Abonnement;
import com.example.pi.entity.AbonnementRequest;
import com.example.pi.entity.Pack;
import com.example.pi.entity.UserInfo;
import com.example.pi.repository.AbonnementRepository;
import com.example.pi.repository.AbonnementRequestRepository;
import com.example.pi.repository.PackRepository;
import com.example.pi.repository.UserInfoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class AbonnementRequestService {

    private static final Logger logger = LoggerFactory.getLogger(AbonnementRequestService.class);
    private final AbonnementRequestRepository requestRepository;
    private final AbonnementRepository abonnementRepository;
    private final PackRepository packRepository;
    private final UserInfoRepository userRepository;


    private UserInfo getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username).orElse(null);

    }


    // Demande d’abonnement par GymGoer
    public AbonnementRequest createRequest(Long packId) {
        UserInfo currentUser = getCurrentUser();
        Optional<Pack> optionalPack = packRepository.findById(packId);

        if (currentUser == null || optionalPack.isEmpty()) return null;

        AbonnementRequest request = new AbonnementRequest();
        request.setUser(currentUser);
        request.setPack(optionalPack.get());
        request.setRequestedDate(LocalDate.now());
        request.setStartDate(LocalDate.now());  // Utilisation de la date actuelle
        int durationDays = optionalPack.get().getDuration(); // Durée du pack en jours
        request.setEndDate(LocalDate.now().plusDays(durationDays));  // Calcul de la date de fin

        request.setStatus(AbonnementRequest.RequestStatus.PENDING);

        return requestRepository.save(request);
    }




    // Validation par Club Owner
    public Abonnement validateRequest(Long requestId) {
        Optional<AbonnementRequest> optionalRequest = requestRepository.findById(requestId);
        if (optionalRequest.isEmpty()) return null;

        AbonnementRequest request = optionalRequest.get();
        UserInfo currentUser = getCurrentUser();

        if (request.getPack().getClub().getOwner().getId() != currentUser.getId()) return null;

        request.setStatus(AbonnementRequest.RequestStatus.APPROVED);
        requestRepository.save(request);

        // Création de l'abonnement
        Abonnement abonnement = new Abonnement();
        abonnement.setUser(request.getUser());
        abonnement.setPack(request.getPack());
        abonnement.setStartDate(LocalDate.now());
        // ➡️ Incrémenter subscriptionCount du Pack
        Pack pack = request.getPack();
        pack.setSubscriptionCount(pack.getSubscriptionCount() + 1);
        packRepository.save(pack);

        // Utilisation de la durée du pack pour définir la date de fin
        int durationDays = request.getPack().getDuration();
        abonnement.setEndDate(LocalDate.now().plusDays(durationDays));
        abonnement.setStatus("active");
        abonnement.setEndDateOfRenewal(null);

        Abonnement savedAbonnement = abonnementRepository.save(abonnement);


        return savedAbonnement;
    }




    // Rejeter une demande
    public AbonnementRequest rejectRequest(Long requestId) {
        AbonnementRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Demande non trouvée"));
        request.setStatus(AbonnementRequest.RequestStatus.REJECTED);  // Changer le statut à "rejeté"
        return requestRepository.save(request);  // Sauvegarder la demande rejetée dans le repository
    }

    // Liste des demandes pour un Club Owner
    public List<AbonnementRequest> getRequestsForOwner() {
        UserInfo currentUser = getCurrentUser();
        return requestRepository.findByPack_Club_Owner_Id(currentUser.getId());
    }
}
