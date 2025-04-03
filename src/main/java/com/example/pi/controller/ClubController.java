package com.example.pi.controller;

import com.example.pi.entity.Club;
import com.example.pi.service.ClubService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;

    @GetMapping("/retrieve-all-clubs")
    public List<Club> getAllClubs() {
        return clubService.getAllClubs();
    }

    @GetMapping("/retrieve-club/{id}")
    public Club getClubById(@PathVariable Long id) {
        return clubService.getClubById(id);
    }

    @PostMapping("/add-club")
    public Club addClub(@RequestBody Club club) {
        return clubService.createClub(club);
    }

    @PutMapping("/update-club/{id}")
    public Club updateClub(@PathVariable Long id, @RequestBody Club club) {
        return clubService.updateClub(id, club);
    }

    @DeleteMapping("/remove-club/{id}")
    public void removeClub(@PathVariable Long id) {
        clubService.deleteClub(id);
    }

    @PostMapping("/{clubId}/sports/{sportId}")
    public Club affecterSportToClub(@PathVariable Long clubId, @PathVariable Long sportId) {
        return clubService.affecterSportToClub(clubId, sportId);
    }
}
