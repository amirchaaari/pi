package com.example.pi.controller;

import com.example.pi.entity.Club;
import com.example.pi.entity.ClubCreationRequest;
import com.example.pi.service.AbonnementService;
import com.example.pi.service.ClubService;
import com.example.pi.service.PackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/clubs")
public class ClubController {

    @Autowired
    private final ClubService clubService;
    private final AbonnementService abonnementService;

    @GetMapping("/retrieve-all-clubs")
    public ResponseEntity<?> getAllClubs() {
        try {
            List<Club> clubs = clubService.getAllClubs();
            return ResponseEntity.ok(clubs);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/retrieve-club/{id}")
    public ResponseEntity<?> getClubById(@PathVariable Long id) {
        try {
            Club club = clubService.getClubById(id);
            return ResponseEntity.ok(club);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/add-club")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<?> addClub(@RequestBody Club club) {
        try {
            Club savedClub = clubService.createClub(club);
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedClub);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/update-club/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_ADMIN')")
    public ResponseEntity<?> updateClub(

            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("capacity") int capacity,
            @RequestParam("status") Club.RequestStatus status,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        try {
            // Créer un objet Club avec les nouvelles données
            Club updatedClub = new Club();
            updatedClub.setName(name);
            updatedClub.setDescription(description);
            updatedClub.setCapacity(capacity);
            updatedClub.setStatus(status);

            // Appel à la méthode du service pour mettre à jour le club
            Club updatedClubResponse = clubService.updateClub(id, updatedClub, imageFile);

            return ResponseEntity.ok(updatedClubResponse);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    @DeleteMapping("/remove-club/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_ADMIN')")
    public ResponseEntity<?> removeClub(@PathVariable Long id) {
        try {
            clubService.deleteClub(id);
            return ResponseEntity.ok(Map.of("message", "Club deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{clubId}/sports/{sportId}")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<?> affecterSportToClub(@PathVariable Long clubId, @PathVariable Long sportId) {
        try {
            Club updatedClub = clubService.affecterSportToClub(clubId, sportId);
            if (updatedClub == null) {
                return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Club or Sport not found"));
            }
            return ResponseEntity.ok(updatedClub);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/submit-creation-request")
    //@PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<?> submitClubCreationRequest(
            @RequestPart("request") String requestJson,
            @RequestParam(value = "document", required = false) MultipartFile document,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            // Convertir le JSON en objet ClubCreationRequest
            ClubCreationRequest request = objectMapper.readValue(requestJson, ClubCreationRequest.class);

            // Appeler le service pour soumettre la demande de création de club
            ClubCreationRequest creationRequest = clubService.submitClubCreationRequest(request, document, image);

            if (creationRequest == null) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error", "Unable to submit club creation request"));
            }

            // Retourner la réponse avec le statut CREATED et la demande créée
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(creationRequest);

        } catch (Exception e) {
            // Gérer les erreurs et retourner un message d'erreur avec le statut BAD_REQUEST
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }



    @GetMapping("/admin/document/{requestId}")
    public ResponseEntity<byte[]> getDocument(@PathVariable Long requestId) {
        byte[] documentData = clubService.getClubRequestDocument(requestId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=document.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(documentData);
    }



    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/pending-requests")
    public ResponseEntity<?> getPendingRequests() {
        try {
            List<ClubCreationRequest> requests = clubService.getPendingClubCreationRequests();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/admin/approve/{requestId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> approveClubCreationRequest(@PathVariable Long requestId) {
        try {
            Club club = clubService.approveClubCreationRequest(requestId);
            if (club == null) {
                return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Unable to approve club creation request"));
            }
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(club);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/admin/reject/{requestId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> rejectClubCreationRequest(@PathVariable Long requestId) {
        try {
            boolean rejected = clubService.rejectClubCreationRequest(requestId);
            if (!rejected) {
                return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Unable to reject club creation request"));
            }
            return ResponseEntity.ok(Map.of("message", "Club creation request rejected successfully"));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }




    @GetMapping("/recommended-ids")
    @PreAuthorize("hasRole('ROLE_USER')")
    public  List<Club> getRecommendedClubIds() {
        try {
            return clubService.recommanderClubsPourUtilisateur();
        } catch (Exception e) {
           throw new RuntimeException("Error while fetching recommended club IDs: " + e.getMessage());
        }
    }
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getClubImage(@PathVariable Long id) {
        byte[] image = clubService.getClubImage(id); // récupère le BLOB
        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // ou PNG si besoin

        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }
    @GetMapping("/occupancy-rates")
    public List<Map<String, Object>> getAllClubsOccupancyRate() {
        return clubService.calculateAllClubsOccupancyRate();
    }


    @GetMapping("/my-club")
    public ResponseEntity<Club> getClubForOwner() {
        Club club = clubService.getClubForAuthenticatedOwner();
        return ResponseEntity.ok(club);
    }


}
