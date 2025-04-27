package com.example.pi.controller;

import com.example.pi.entity.Trophy;
import com.example.pi.entity.UserInfo;
import com.example.pi.service.TrophyService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trophies")
@RequiredArgsConstructor
public class TrophyController {

    private final TrophyService trophyService;

    @GetMapping("/retrieve-all-trophies")
    public List<Trophy> getAll() {
        return trophyService.getAllTrophies();
    }

    @GetMapping("/retrieve-trophy/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Trophy> getById(@PathVariable Long id) {
        return trophyService.getTrophyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add-trophy")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Trophy> create(@RequestBody Trophy trophy) {
        return ResponseEntity.ok(trophyService.createTrophy(trophy));
    }

    @PutMapping("/update-trophy/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Trophy> update(@PathVariable Long id, @RequestBody Trophy trophy) {
        Trophy updated = trophyService.updateTrophy(id, trophy);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }

    @DeleteMapping("/remove-trophy/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return trophyService.deleteTrophy(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @PutMapping("/updatePoints/{userId}/{newPoints}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserInfo> updateUserPoints(@PathVariable int userId, @PathVariable int newPoints) {
        return ResponseEntity.ok(trophyService.updateUserPoints(userId, newPoints));
    }

    @GetMapping("/my-trophies")
    @PreAuthorize("hasRole('ROLE_USER')")
    public HashMap<String, Object> getMyTrophies() {
        UserInfo user = trophyService.getCurrentUser();

        HashMap<String, Object> trophies = new HashMap<>();
        trophies.put("points", user.getPoints());
        trophies.put("trophies", user.getTrophies().stream()
                .map(trophy -> {
                    HashMap<String, Object> trophyMap = new HashMap<>();
                    trophyMap.put("id", trophy.getId());
                    trophyMap.put("name", trophy.getName());
                    trophyMap.put("description", trophy.getDescription());
                    return trophyMap;
                })
                .collect(Collectors.toList()));
        return trophies;

    }
    @PostMapping("/assignTrophy")
    public ResponseEntity<UserInfo> assignTrophyToUser() {
        try {
            UserInfo user = trophyService.assignTrophyToUser();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'attribution des trophées dans le contrôleur : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
