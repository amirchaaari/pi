package com.example.pi.service;

import com.example.pi.entity.Abonnement;
import com.example.pi.entity.Pack;
import com.example.pi.entity.Trophy;
import com.example.pi.entity.UserInfo;
import com.example.pi.interfaces.IAbonnementService;
import com.example.pi.repository.AbonnementRepository;
import com.example.pi.repository.PackRepository;
import com.example.pi.repository.TrophyRepository;
import com.example.pi.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrophyService {

    private final TrophyRepository trophyRepository;
    private final UserInfoRepository userInfoRepository;

    public List<Trophy> getAllTrophies() {
        return trophyRepository.findAll();
    }

    public Optional<Trophy> getTrophyById(Long id) {
        return trophyRepository.findById(id);
    }

    public Trophy createTrophy(Trophy trophy) {
        return trophyRepository.save(trophy);
    }

    public Trophy updateTrophy(Long id, Trophy updated) {
        return trophyRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setDescription(updated.getDescription());
            existing.setRequiredPoints(updated.getRequiredPoints());
            return trophyRepository.save(existing);
        }).orElse(null);
    }

    public boolean deleteTrophy(Long id) {
        if (trophyRepository.existsById(id)) {
            trophyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Attribution automatique de trophées
    public UserInfo updateUserPoints(int userId, int newPoints) {
        UserInfo user = userInfoRepository.findById(userId).orElseThrow();

        user.setPoints(newPoints);
        List<Trophy> allTrophies = trophyRepository.findAll();

        for (Trophy trophy : allTrophies) {
            if (newPoints >= trophy.getRequiredPoints() && !user.getTrophies().contains(trophy)) {
                user.getTrophies().add(trophy);
            }
        }

        return userInfoRepository.save(user);
    }

    // Pour récupérer l'utilisateur connecté
    public UserInfo getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userInfoRepository.findByEmail(username).orElse(null);
    }

    public void updateUserTrophies(UserInfo user) {
        Set<Trophy> earned = user.getTrophies();


        Set<Trophy> allTrophies = trophyRepository.findAll().stream()
                .filter(trophy -> !earned.contains(trophy) && user.getPoints() >= trophy.getRequiredPoints())
                .collect(Collectors.toSet());


        if (!allTrophies.isEmpty()) {
            earned.addAll(allTrophies);
            user.setTrophies(earned);
            userInfoRepository.save(user);
        }

    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void claimTrophies() {
        List<UserInfo> users = userInfoRepository.findAll();
        for (UserInfo user : users) {
            updateUserTrophies(user);
        }
    }










}

