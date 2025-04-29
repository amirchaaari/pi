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
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClubService  implements IClubService {

    private final ClubRepository clubRepository;
    private final ClubCreationRequestRepository clubCreationRequestRepository;
    private final UserInfoRepository userRepository;
    private final SportRepository sportRepository;

    private UserInfo getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username).orElse(null);
    }

    public ClubCreationRequest submitClubCreationRequest(ClubCreationRequest request, MultipartFile documentFile, MultipartFile imageFile) {
        UserInfo authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser == null || !authenticatedUser.getRoles().contains("ROLE_CLUB_OWNER")) {
            return null;
        }

        try {
            byte[] documentBytes = documentFile.getBytes();
            byte[] imageBytes = imageFile != null ? imageFile.getBytes() : null;

            request.setClubOwner(authenticatedUser);
            request.setStatus(ClubCreationRequest.RequestStatus.PENDING);
            request.setDocument(documentBytes);
            request.setImage(imageBytes);

            return clubCreationRequestRepository.save(request);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Club approveClubCreationRequest(Long requestId) {
        Optional<ClubCreationRequest> optionalRequest = clubCreationRequestRepository.findById(requestId);
        if (optionalRequest.isEmpty()) {
            return null;
        }

        ClubCreationRequest request = optionalRequest.get();
        UserInfo authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser == null || !authenticatedUser.getRoles().contains("ROLE_ADMIN")) {
            return null;
        }

        request.setStatus(ClubCreationRequest.RequestStatus.APPROVED);
        clubCreationRequestRepository.save(request);

        Club club = new Club();
        club.setName(request.getName());
        club.setDescription(request.getDescription());
        club.setCapacity(request.getCapacity());
        club.setOwner(request.getClubOwner());
        club.setStatus(Club.RequestStatus.APPROVED);
        club.setImage(request.getImage());

        return clubRepository.save(club);
    }


    public boolean rejectClubCreationRequest(Long requestId) {
        Optional<ClubCreationRequest> optionalRequest = clubCreationRequestRepository.findById(requestId);
        if (optionalRequest.isEmpty()) {
            return false;
        }

        ClubCreationRequest request = optionalRequest.get();
        UserInfo authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser == null || !authenticatedUser.getRoles().contains("ROLE_ADMIN")) {
            return false;
        }

        request.setStatus(ClubCreationRequest.RequestStatus.REJECTED);
        clubCreationRequestRepository.save(request);
        return true;
    }


    public List<ClubCreationRequest> getPendingClubCreationRequests() {
        return clubCreationRequestRepository.findByStatus(ClubCreationRequest.RequestStatus.PENDING);
    }

    public Club affecterSportToClub(Long clubId, Long sportId) {
        Optional<Sport> sportOpt = sportRepository.findById(sportId);
        if (sportOpt.isEmpty()) {
            return null;
        }

        Optional<Club> clubOpt = clubRepository.findById(clubId);
        if (clubOpt.isEmpty()) {
            return null;
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

        //  Dissocier les sports avant suppression
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
        club.setStatus(Club.RequestStatus.PENDING);
        return clubRepository.save(club);
    }

    public Club updateClub(Long id, Club updatedClub, MultipartFile imageFile) {
        Optional<Club> existingClubOpt = clubRepository.findById(id);
        UserInfo currentUser = getAuthenticatedUser();

        if (existingClubOpt.isEmpty()) {
            throw new RuntimeException("Club not found with id: " + id);
        }

        Club existingClub = existingClubOpt.get();

        // Vérifie si l'utilisateur est propriétaire ou admin
        if (!existingClub.getOwner().equals(currentUser) &&
                !currentUser.getRoles().contains("ROLE_ADMIN")) {
            throw new RuntimeException("Not authorized to update this club");
        }

        existingClub.setName(updatedClub.getName());
        existingClub.setDescription(updatedClub.getDescription());
        existingClub.setCapacity(updatedClub.getCapacity());

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                existingClub.setImage(imageFile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        return clubRepository.save(existingClub);
    }

    @Override
    public byte[] getClubImage(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club not found with id: " + id));

        if (club.getImage() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No image found for this club");
        }

        return club.getImage();
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

    public List<Map<String, Object>> calculateAllClubsOccupancyRate() {
        List<Club> clubs = clubRepository.findAll();

        List<Map<String, Object>> occupancyRates = new ArrayList<>();

        // Parcourir tous les clubs et calculer le taux d'occupation pour chacun
        for (Club club : clubs) {
            int capacity = club.getCapacity();
            if (capacity == 0) {
                occupancyRates.add(createOccupancyRateMap(club.getId(), 0.0));
                continue; // Si la capacité est 0, on passe au club suivant
            }

            // Compter tous les abonnements liés aux packs du club
            int totalSubscriptions = club.getPacks().stream()
                    .flatMap(pack -> pack.getAbonnements().stream())
                    .mapToInt(abonnement -> 1) // chaque abonnement compte pour 1
                    .sum();

            double occupancyRate = (double) totalSubscriptions / capacity * 100;

            // Arrondi à 2 chiffres après la virgule
            occupancyRates.add(createOccupancyRateMap(club.getId(), Math.round(occupancyRate * 100.0) / 100.0));
        }

        return occupancyRates;
    }

    private Map<String, Object> createOccupancyRateMap(Long clubId, Double occupancyRate) {
        Map<String, Object> rateMap = new HashMap<>();
        rateMap.put("clubId", clubId);
        rateMap.put("occupancyRate", occupancyRate);
        return rateMap;
    }

    public Club getClubForAuthenticatedOwner() {
        UserInfo currentUser = getAuthenticatedUser();

        if (currentUser == null || !currentUser.getRoles().contains("ROLE_CLUB_OWNER")) {
            throw new RuntimeException("Unauthorized or not a club owner");
        }

        return clubRepository.findByOwner(currentUser).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No club found for this owner"));
    }




}
