package com.example.pi.controller;

import com.example.pi.entity.Club;
import com.example.pi.entity.ClubCreationRequest;
import com.example.pi.service.ClubService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/clubs")
@CrossOrigin(origins = "*")
public class ClubController {

    private final ClubService clubService;

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
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER')")
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
    @PreAuthorize("hasAnyRole('ROLE_CLUB_OWNER', 'ROLE_ADMIN')")
    public ResponseEntity<?> updateClub(@PathVariable Long id, @RequestBody Club club) {
        try {
            Club updatedClub = clubService.updateClub(id, club);
            return ResponseEntity.ok(updatedClub);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/remove-club/{id}")
    @PreAuthorize("hasAnyRole('ROLE_CLUB_OWNER', 'ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER')")
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

    @PostMapping("/submit-creation-request")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER')")
    public ResponseEntity<?> submitClubCreationRequest(@RequestBody ClubCreationRequest request) {
        try {
            ClubCreationRequest creationRequest = clubService.submitClubCreationRequest(request);
            if (creationRequest == null) {
                return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Unable to submit club creation request"));
            }
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creationRequest);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/admin/pending-requests")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
}
