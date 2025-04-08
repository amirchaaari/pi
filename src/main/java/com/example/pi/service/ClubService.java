package com.example.pi.service;

import com.example.pi.entity.Club;
import com.example.pi.entity.ClubCreationRequest;
import com.example.pi.entity.Sport;
import com.example.pi.entity.UserInfo;
import com.example.pi.repository.ClubRepository;
import com.example.pi.repository.ClubCreationRequestRepository;
import com.example.pi.repository.SportRepository;
import com.example.pi.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubCreationRequestRepository clubCreationRequestRepository;
    private final UserInfoRepository userRepository;
    private final SportRepository sportRepository;

    private UserInfo getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    // Soumettre une demande de création de club
    public ClubCreationRequest submitClubCreationRequest(ClubCreationRequest request) {
        UserInfo authenticatedUser = getAuthenticatedUser();

        // Vérifier que l'utilisateur est bien un ClubOwner
        if (!authenticatedUser.getRoles().contains("ROLE_CLUB_OWNER")) {
            throw new RuntimeException("Seul un ClubOwner peut soumettre une demande de création de club.");
        }

        request.setClubOwner(authenticatedUser);
        request.setStatus(ClubCreationRequest.RequestStatus.PENDING);
        return clubCreationRequestRepository.save(request);
    }

    // Approuver une demande de création de club et créer le club
    public Club approveClubCreationRequest(Long requestId) {
        ClubCreationRequest request = clubCreationRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        UserInfo authenticatedUser = getAuthenticatedUser();

        // Vérifier que l'utilisateur est bien un admin
        if (!authenticatedUser.getRoles().contains("ROLE_ADMIN")) {
            throw new RuntimeException("Seul un administrateur peut approuver une demande.");
        }

        request.setStatus(ClubCreationRequest.RequestStatus.APPROVED);
        clubCreationRequestRepository.save(request);

        Club club = new Club();
        club.setName(request.getName());
        club.setDescription(request.getDescription());
        club.setCapacity(request.getCapacity());
        club.setOwner(request.getClubOwner());

        return clubRepository.save(club);
    }

    // Refuser une demande de création de club
    public void rejectClubCreationRequest(Long requestId) {
        ClubCreationRequest request = clubCreationRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        UserInfo authenticatedUser = getAuthenticatedUser();

        // Vérifier que l'utilisateur est bien un admin
        if (!authenticatedUser.getRoles().contains("ROLE_ADMIN")) {
            throw new RuntimeException("Seul un administrateur peut refuser une demande.");
        }

        request.setStatus(ClubCreationRequest.RequestStatus.REJECTED);
        clubCreationRequestRepository.save(request);
    }

    // Récupérer toutes les demandes en attente
    public List<ClubCreationRequest> getPendingClubCreationRequests() {
        return clubCreationRequestRepository.findByStatus(ClubCreationRequest.RequestStatus.PENDING);
    }

    // Affecter un sport à un club
    public Club affecterSportToClub(Long clubId, Long sportId) {
        Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new RuntimeException("Sport non trouvé"));

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club non trouvé"));

        Set<Sport> sportsMisesAJour = Optional.ofNullable(club.getSports()).orElse(new HashSet<>());
        sportsMisesAJour.add(sport);

        club.setSports(sportsMisesAJour);

        return clubRepository.save(club);
    }

    // Supprimer un club
    public void deleteClub(Long id) {
        Club club = clubRepository.findById(id).orElseThrow(() -> new RuntimeException("Club non trouvé"));
        clubRepository.delete(club);
    }

    // Récupérer un club par son ID
    public Club getClubById(Long id) {
        return clubRepository.findById(id).orElseThrow(() -> new RuntimeException("Club non trouvé"));
    }

    // Récupérer tous les clubs
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    // Créer un club
    public Club createClub(Club club) {
        return clubRepository.save(club);
    }

    // Mettre à jour un club
    public Club updateClub(Long id, Club club) {
        Club existingClub = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club non trouvé"));

        existingClub.setName(club.getName());
        existingClub.setDescription(club.getDescription());
        existingClub.setCapacity(club.getCapacity());

        return clubRepository.save(existingClub);
    }
}
