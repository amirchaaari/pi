package com.example.pi.controller;

import com.example.pi.entity.Sport;
import com.example.pi.service.SportService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/sports")
public class SportController {

    private final SportService sportService;

    @GetMapping("/retrieve-all-sports")
    public List<Sport> getAllSports() {
        return sportService.getAllSports();
    }

    @GetMapping("/retrieve-sport/{id}")
    public Sport getSportById(@PathVariable Long id) {
        return sportService.getSportById(id);
    }

    @PostMapping("/add-sport")
    public Sport addSport(@RequestBody Sport sport) {
        return sportService.createSport(sport);
    }

    @PutMapping("/update-sport/{id}")
    public Sport updateSport(@PathVariable Long id, @RequestBody Sport sport) {
        return sportService.updateSport(id, sport);
    }

    @DeleteMapping("/remove-sport/{id}")
    public void removeSport(@PathVariable Long id) {
        sportService.deleteSport(id);
    }
}
