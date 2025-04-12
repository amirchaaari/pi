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

    // Méthode pour récupérer l'utilisateur actuellement authentifié
    private UserInfo getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username).orElse(null);  // Retourne null si l'utilisateur n'est pas trouvé
    }

    // Soumettre une demande de création de club
    public ClubCreationRequest submitClubCreationRequest(ClubCreationRequest request) {
        UserInfo authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser == null || !authenticatedUser.getRoles().contains("ROLE_CLUB_OWNER")) {
            return null;  // Retourne null si l'utilisateur n'est pas authentifié ou n'est pas un ClubOwner
        }

        // Associe le ClubOwner à la demande et définit son statut à PENDING
        request.setClubOwner(authenticatedUser);
        request.setStatus(ClubCreationRequest.RequestStatus.PENDING);
        return clubCreationRequestRepository.save(request);
    }

    // Approuver une demande de création de club et créer le club associé
    public Club approveClubCreationRequest(Long requestId) {
        Optional<ClubCreationRequest> optionalRequest = clubCreationRequestRepository.findById(requestId);
        if (optionalRequest.isEmpty()) {
            return null;  // Retourne null si la demande n'est pas trouvée
        }

        ClubCreationRequest request = optionalRequest.get();
        UserInfo authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser == null || !authenticatedUser.getRoles().contains("ROLE_ADMIN")) {
            return null;  // Retourne null si l'utilisateur n'est pas un Admin
        }

        // Modifie le statut de la demande et sauvegarde la mise à jour
        request.setStatus(ClubCreationRequest.RequestStatus.APPROVED);
        clubCreationRequestRepository.save(request);

        // Crée un nouveau club avec les informations de la demande approuvée
        Club club = new Club();
        club.setName(request.getName());
        club.setDescription(request.getDescription());
        club.setCapacity(request.getCapacity());
        club.setOwner(request.getClubOwner());
        club.setStatus(Club.RequestStatus.APPROVED);
        return clubRepository.save(club);
    }

    // Refuser une demande de création de club
    public boolean rejectClubCreationRequest(Long requestId) {
        Optional<ClubCreationRequest> optionalRequest = clubCreationRequestRepository.findById(requestId);
        if (optionalRequest.isEmpty()) {
            return false;  // Retourne false si la demande n'est pas trouvée
        }

        ClubCreationRequest request = optionalRequest.get();
        UserInfo authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser == null || !authenticatedUser.getRoles().contains("ROLE_ADMIN")) {
            return false;  // Retourne false si l'utilisateur n'est pas un Admin
        }

        // Modifie le statut de la demande à REJECTED et sauvegarde la mise à jour
        request.setStatus(ClubCreationRequest.RequestStatus.REJECTED);
        clubCreationRequestRepository.save(request);
        return true;
    }

    // Récupérer toutes les demandes en attente
    public List<ClubCreationRequest> getPendingClubCreationRequests() {
        return clubCreationRequestRepository.findByStatus(ClubCreationRequest.RequestStatus.PENDING);
    }

    // Affecter un sport à un club
    public Club affecterSportToClub(Long clubId, Long sportId) {
        Optional<Sport> sportOpt = sportRepository.findById(sportId);
        if (sportOpt.isEmpty()) {
            return null;  // Retourne null si le sport n'est pas trouvé
        }

        Optional<Club> clubOpt = clubRepository.findById(clubId);
        if (clubOpt.isEmpty()) {
            return null;  // Retourne null si le club n'est pas trouvé
        }

        Club club = clubOpt.get();
        Sport sport = sportOpt.get();

        // Ajoute le sport au club, en évitant les doublons
        Set<Sport> sportsMisesAJour = Optional.ofNullable(club.getSports()).orElse(new HashSet<>());
        sportsMisesAJour.add(sport);

        club.setSports(sportsMisesAJour);
        return clubRepository.save(club);  // Sauvegarde les changements dans la base de données
    }

    // Supprimer un club
    public boolean deleteClub(Long id) {
        Optional<Club> clubOpt = clubRepository.findById(id);
        UserInfo currentUser = getAuthenticatedUser();

        if (clubOpt.isEmpty()) {
            throw new RuntimeException("Club not found with id: " + id);
        }

        Club club = clubOpt.get();

        // Check if user is owner or admin
        if (!club.getOwner().equals(currentUser) &&
                !currentUser.getRoles().contains("ROLE_ADMIN")) {
            throw new RuntimeException("Not authorized to delete this club");
        }

        // 🔄 Dissocier les sports avant suppression
        for (Sport sport : club.getSports()) {
            sport.getClubs().remove(club);
        }
        club.getSports().clear();

        clubRepository.delete(club);
        return true;
    }


    public Club getClubById(Long id) {
        return clubRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Club not found with id: " + id));
    }

    public List<Club> getAllClubs() {
        UserInfo currentUser = getAuthenticatedUser();
        
        // If user is admin, return all clubs
        if (currentUser.getRoles().contains("ROLE_ADMIN")) {
            return clubRepository.findAll();
        }
        
        // If user is club owner, return only their clubs
        if (currentUser.getRoles().contains("ROLE_CLUB_OWNER")) {
            return clubRepository.findByOwner(currentUser);
        }
        
        // For regular users, return only approved clubs
        return clubRepository.findByStatus(Club.RequestStatus.APPROVED);
    }

    public Club createClub(Club club) {
        UserInfo owner = getAuthenticatedUser();
        if (owner == null) {
            throw new RuntimeException("User not authenticated");
        }
        club.setOwner(owner);
        club.setStatus(Club.RequestStatus.PENDING); // New clubs start as pending
        return clubRepository.save(club);
    }

    public Club updateClub(Long id, Club updatedClub) {
        Optional<Club> existingClubOpt = clubRepository.findById(id);
        UserInfo currentUser = getAuthenticatedUser();

        if (existingClubOpt.isEmpty()) {
            throw new RuntimeException("Club not found with id: " + id);
        }

        Club existingClub = existingClubOpt.get();
        
        // Check if user is owner or admin
        if (!existingClub.getOwner().equals(currentUser) && 
            !currentUser.getRoles().contains("ROLE_ADMIN")) {
            throw new RuntimeException("Not authorized to update this club");
        }

        existingClub.setName(updatedClub.getName());
        existingClub.setDescription(updatedClub.getDescription());
        existingClub.setCapacity(updatedClub.getCapacity());
        
        return clubRepository.save(existingClub);
    }
}
