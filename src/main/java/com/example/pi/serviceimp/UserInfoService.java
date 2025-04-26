package com.example.pi.serviceimp;


import com.example.pi.entity.Status;
import com.example.pi.entity.UserInfo;
import com.example.pi.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Primary
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

    public UserInfo getUserById(int receiverId) {
        return repository.findById(receiverId).orElse(null);
    }

    public UserInfo getUserByUsername(String name) {
        return repository.findByEmail(name).orElse(null);
    }

    public List<UserInfo> getUsersByRole(String roleCoach) {
        return repository.findByRoles(roleCoach);
    }
    public List<UserInfo> findConnectedUsers() {
        return repository.findAll();
    }
    public UserInfo getUser() {
        // Récupérer l'authentification du SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserInfoDetails) {
            // Récupérer les détails de l'utilisateur
            UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();

            // Utiliser l'email ou l'id de l'utilisateur pour récupérer l'entité complète de l'utilisateur
            UserInfo user = repository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            System.out.println("→ Currentuser: " + user);
            return user;

        }

        throw new RuntimeException("User not authenticated");
    }
    public void updateStatus(String email, Status status) {
        UserInfo user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setStatus(status);
        user.setLastLogin(LocalDateTime.now());
        repository.save(user);
    }
}
