package com.example.pi.controller.Livraison;

import com.example.pi.entity.Livreur;
import com.example.pi.service.LivraisonService.LivreurService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livreurs")
@AllArgsConstructor
public class LivreurController {
    private final LivreurService livreurService;

    @PostMapping
    public ResponseEntity<Livreur> createDriver(@RequestBody Livreur livreur) {
        return ResponseEntity.ok(livreurService.registerDriver(livreur));
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<Livreur> updateAvailability(
            @PathVariable Long id,
            @RequestParam boolean available) {
        return ResponseEntity.ok(livreurService.updateAvailability(id, available));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Livreur>> getAvailableDrivers() {
        return ResponseEntity.ok(livreurService.getAvailableDrivers());
    }
}
