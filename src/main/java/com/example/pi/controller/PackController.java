package com.example.pi.controller;

import com.example.pi.entity.Club;
import com.example.pi.entity.Pack;
import com.example.pi.service.PackService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Pack addPack(@RequestBody Pack pack) {
        return packService.createPack(pack);
    }

    @PutMapping("/update-pack/{id}")
    public Pack updatePack(@PathVariable Long id, @RequestBody Pack pack) {
        return packService.updatePack(id, pack);
    }

    @DeleteMapping("/remove-pack/{id}")
    public void removePack(@PathVariable Long id) {
        packService.deletePack(id);
    }

    @PutMapping("/affect-pack/{packId}/to-club/{clubId}")
    public Club affecterPackToClub(@PathVariable Long clubId, @PathVariable Long packId) {
        return packService.affecterPackToClub(clubId, packId);
    }

}
