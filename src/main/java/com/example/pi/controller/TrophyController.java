package com.example.pi.controller;

import com.example.pi.entity.Trophy;
import com.example.pi.entity.UserInfo;
import com.example.pi.service.TrophyService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/trophies")
@RequiredArgsConstructor
public class TrophyController {

    private final TrophyService trophyService;

    @GetMapping("/retrieve-all-trophies")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    public Set<Trophy> getMyTrophies() {
        UserInfo user = trophyService.getCurrentUser();
        return user != null ? user.getTrophies() : Collections.emptySet();
    }
}
