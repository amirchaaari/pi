package com.example.pi.controller;

import com.example.pi.entity.Club;
import com.example.pi.entity.Pack;
import com.example.pi.service.PackService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/packs")
public class PackController {

    private final PackService packService;

    @GetMapping("/retrieve-all-packs")
    public List<Pack> getAllPacks() {
        return packService.getAllPacks();
    }

    @GetMapping("/retrieve-pack/{id}")
    public Pack getPackById(@PathVariable Long id) {
        return packService.getPackById(id);
    }

    @PostMapping("/add-pack")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER')")
    public Pack addPack(@RequestBody Pack pack) {
        return packService.createPack(pack);
    }

    @PutMapping("/update-pack/{id}")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER')")
    public Pack updatePack(@PathVariable Long id, @RequestBody Pack pack) {
        return packService.updatePack(id, pack);
    }

    @DeleteMapping("/remove-pack/{id}")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER')")
    public void removePack(@PathVariable Long id) {
        packService.deletePack(id);
    }

    @PutMapping("/affect-pack/{packId}/to-club/{clubId}")
    @PreAuthorize("hasRole('ROLE_CLUB_OWNER')")
    public Club affecterPackToClub(@PathVariable Long clubId, @PathVariable Long packId) {
        return packService.affecterPackToClub(clubId, packId);
    }

    @GetMapping("/popularity")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Pack> getPopularPacks() {
        return packService.getPacksByPopularity();
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Map<String, Object> getPacksPopularityStatistics() {
        return packService.getPacksPopularityStatistics();
    }

}
