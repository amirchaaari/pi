package com.example.pi.service;

import com.example.pi.entity.*;
import com.example.pi.interfaces.IClubService;
import com.example.pi.repository.ClubRepository;
import com.example.pi.repository.ClubCreationRequestRepository;
import com.example.pi.repository.SportRepository;
import com.example.pi.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClubService  implements IClubService {

    private final ClubRepository clubRepository;
    private final ClubCreationRequestRepository clubCreationRequestRepository;
    private final UserInfoRepository userRepository;
    private final SportRepository sportRepository;

    // Méthode pour récupérer l'utilisateur actuellement authentifié
    private UserInfo getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username).orElse(null);  // Retourne null si l'utilisateur n'est pas trouvé
    }

    // Soumettre une demande de création de club avec un document //
    public ClubCreationRequest submitClubCreationRequest(ClubCreationRequest request, MultipartFile documentFile) {
        UserInfo authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser == null || !authenticatedUser.getRoles().contains("ROLE_CLUB_OWNER")) {
            return null;  // Retourne null si l'utilisateur n'est pas authentifié ou n'est pas un ClubOwner
        }

        try {
            // Convertir le fichier en tableau de bytes
            byte[] documentBytes = documentFile.getBytes();

            // Associe le ClubOwner à la demande et définit son statut à PENDING
            request.setClubOwner(authenticatedUser);
            request.setStatus(ClubCreationRequest.RequestStatus.PENDING);
            request.setDocument(documentBytes);

            // Sauvegarde la demande de création de club avec le document
            return clubCreationRequestRepository.save(request);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Retourne null si l'upload du document échoue
        }
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
    public void deleteClub(Long id) {
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


    public void calculateStatistics(Long clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(() -> new RuntimeException("Club non trouvé"));

        Set<Pack> packs = club.getPacks();
        int totalSubscriptions = 0;
        int totalPacks = packs.size();
        for (Pack pack : packs) {
            totalSubscriptions += pack.getAbonnements().size();  // Compte les abonnements pour chaque pack
        }

        double averageSubscriptionsPerPack = totalPacks > 0 ? (double) totalSubscriptions / totalPacks : 0;

        club.setTotalSubscriptions(totalSubscriptions);
        club.setTotalPacks(totalPacks);
        club.setAverageSubscriptionsPerPack(averageSubscriptionsPerPack);

        clubRepository.save(club);  // Sauvegarde les modifications
    }


    public List<Club> recommanderClubsPourUtilisateur() {
        // Étape 1 : Récupérer les abonnements de l'utilisateur
        UserInfo user = getAuthenticatedUser();
        if (user == null) {
            throw new RuntimeException("User not authenticated");
        }
        Set<Abonnement> abonnements = user.getAbonnements();

        // Étape 2 : Récupérer les packs abonnés
        Set<Pack> packs = abonnements.stream()
                .map(Abonnement::getPack)
                .collect(Collectors.toSet());

        // Étape 3 : Récupérer les clubs abonnés
        Set<Club> clubsAbonnes = packs.stream()
                .map(Pack::getClub)
                .collect(Collectors.toSet());

        // Étape 4 : Extraire les sports de ces clubs
        Set<Sport> sportsPreferes = clubsAbonnes.stream()
                .flatMap(club -> club.getSports().stream())
                .collect(Collectors.toSet());

        // Étape 5 : Rechercher d'autres clubs proposant ces sports (mais pas déjà abonnés)

        return clubRepository.findAll().stream()
                .filter(club -> !clubsAbonnes.contains(club))
                .filter(club -> club.getSports().stream().anyMatch(sportsPreferes::contains))
                .collect(Collectors.toList());
    }

    public byte[] getClubRequestDocument(Long requestId) {
        ClubCreationRequest request = clubCreationRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club request not found"));

        if (request.getDocument() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No document attached to this request");
        }

        return request.getDocument();
    }



}
