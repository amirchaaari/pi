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

//    private UserInfo getCurrentUser() {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return (principal instanceof UserInfo) ? (UserInfo) principal : null;
//    }
    private UserInfo getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username).orElse(null);

    }


    // Demande d’abonnement par GymGoer
    public AbonnementRequest createRequest(Long packId) {
        UserInfo currentUser = getCurrentUser();
        Optional<Pack> optionalPack = packRepository.findById(packId);

        System.out.println("Creating request for pack " + packId + " by user " + (currentUser != null ? currentUser.getEmail() : "null"));


        if (currentUser == null || optionalPack.isEmpty()) {
            logger.error("Failed to create request: user={}, pack exists={}", 
                currentUser != null, optionalPack.isPresent());
            return null;
        }

        AbonnementRequest request = new AbonnementRequest();
        request.setUser(currentUser);
        request.setPack(optionalPack.get());
        request.setRequestedDate(LocalDate.now());
        request.setStatus(AbonnementRequest.RequestStatus.PENDING);

        AbonnementRequest saved = requestRepository.save(request);
        logger.debug("Created request with id: {}", saved.getId());
        return saved;
    }

    // Validation par Club Owner
    public Abonnement validateRequest(Long requestId) {
        Optional<AbonnementRequest> optional = requestRepository.findById(requestId);
        if (optional.isEmpty()) return null;

        AbonnementRequest request = optional.get();
        UserInfo currentUser = getCurrentUser();

        if (currentUser.getId() != request.getPack().getClub().getOwner().getId()) return null;

        request.setStatus(AbonnementRequest.RequestStatus.APPROVED);
        requestRepository.save(request);

        Abonnement abonnement = new Abonnement();
        abonnement.setUser(request.getUser());
        abonnement.setPack(request.getPack());
        abonnement.setStartDate(LocalDate.now());
        abonnement.setEndDate(LocalDate.now().plusMonths(request.getPack().getDuration()));
        abonnement.setStatus("actif");

        return abonnementRepository.save(abonnement);
    }

    // Liste des demandes pour un Club Owner
    public List<AbonnementRequest> getRequestsForOwner() {
        UserInfo currentUser = getCurrentUser();
        return requestRepository.findByPack_Club_Owner_Id(currentUser.getId());
    }
}
