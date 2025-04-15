package com.example.pi.service;


import com.example.pi.entity.Club;
import com.example.pi.entity.UserInfo;
import com.example.pi.repository.ClubRepository;
import com.example.pi.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByEmail(username); // Assuming 'email' is used as username

        // Converting UserInfo to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
    /*delete a user by id*/

    public void deleteUserById(int id) {
        repository.deleteById(id);
    }

    public String addUser(UserInfo userInfo) {
        // Encode password before saving the user
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Added Successfully";
    }





    public String getUserProfile() {
        // Get authentication from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserInfoDetails) {
            UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();
            return "Welcome " + userDetails.getUsername() + "your id is " + userDetails.getId();
        }

        return "User not authenticated";
    }

    public List<UserInfo> getAllUsers() {
        return repository.findAll();
    }

    public Set<Long> getUserPreferredSportIds(int userId) {
        Optional<UserInfo> optionalUser = repository.findById(userId);
        if (optionalUser.isEmpty()) return new HashSet<>();

        UserInfo user = optionalUser.get();

        // On extrait les sports associés à chaque club auquel l'utilisateur est abonné
        return user.getAbonnements().stream()
                .map(abonnement -> abonnement.getPack().getClub().getSports())  // On obtient les sports du club
                .flatMap(Collection::stream)  // On aplatit la collection de Set<Sport> en Stream<Sport>
                .map(sport -> sport.getId())  // On récupère l'identifiant du sport
                .collect(Collectors.toSet());  // On retourne un Set des identifiants des sports
    }


    @Autowired
    private ClubRepository clubRepository;

    public List<Club> getRecommendedClubs(int userId) {
        Set<Long> preferredSportIds = getUserPreferredSportIds(userId);
        if (preferredSportIds.isEmpty()) return new ArrayList<>();

        // Récupérer l'utilisateur
        Optional<UserInfo> userOpt = repository.findById(userId);
        if (userOpt.isEmpty()) return new ArrayList<>();

        UserInfo user = userOpt.get();

        // Récupérer les IDs des clubs auxquels l'utilisateur est déjà abonné
        Set<Long> clubIdsAlreadySubscribed = user.getAbonnements().stream()
                .map(ab -> ab.getPack().getClub().getId())  // On récupère l'ID du club lié à chaque abonnement
                .collect(Collectors.toSet());

        // Rechercher des clubs qui offrent les sports préférés et qui ne sont pas déjà abonnés
        return clubRepository.findAll().stream()
                .filter(club -> club.getSports().stream()  // Pour chaque club, on parcourt ses sports
                        .anyMatch(sport -> preferredSportIds.contains(sport.getId())))  // On vérifie si un sport du club est préféré
                .filter(club -> !clubIdsAlreadySubscribed.contains(club.getId()))  // On exclut les clubs déjà abonnés
                .collect(Collectors.toList());  // On retourne la liste des clubs recommandés
    }



}
