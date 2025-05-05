package com.example.pi.service;
import com.example.pi.entity.Club;
import com.example.pi.repository.ClubRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import jakarta.mail.MessagingException;


import com.example.pi.entity.Status;
import com.example.pi.entity.UserInfo;
import com.example.pi.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Primary
public class UserInfoService implements UserDetailsService {
    @Autowired
    private EmailService emailService;


    @Autowired
    private UserInfoRepository repository;


    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByEmail(username); // Assuming 'email' is used as username

        // Converting UserInfo to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
    public UserInfo getUserByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }



    public void incrementSessionsAndSaveIt(String email) {
        UserInfo user = repository.findByEmail(email).orElse(null);
        if (user != null) {
            user.setSessions(user.getSessions() + 1);
            user.setLastSession(LocalDateTime.now()); // ⏱ Save current date/time
            repository.save(user);
        }
    }









//    @Scheduled(fixedDelay = 100000000) // Every day at noon
    public void sendReminderEmailToInactiveUsers() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minus(30, ChronoUnit.DAYS);

        // Find all users who haven't logged in for 30 days
        List<UserInfo> inactiveUsers = repository.findByLastSessionBefore(thirtyDaysAgo);

        // Send the reminder email to each inactive user
        for (UserInfo user : inactiveUsers) {
            if ( user.isEnabled()) {
                try {
                    // Load HTML template
                    String html = loadHtmlTemplate("templates/reminder-email.html");
                    // Personalize
                    html = html.replace("{{USERNAME}}", user.getName());
                    // Send
                    emailService.sendHtmlEmail(user.getEmail(), "Training Reminder", html);
                } catch (Exception e) {
                    System.err.println("Failed to send reminder to " + user.getEmail());
                    e.printStackTrace();
                }
            }
        }
    }


//    @Scheduled(fixedDelay = 50000) // Runs every 50 seconds (you can change this)
    public void enforcePasswordResetPolicy() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        List<UserInfo> users = repository.findAll();

        for (UserInfo user : users) {
            if (user.getLastPasswordChange() != null &&
                    user.getLastPasswordChange().isBefore(threeMonthsAgo) &&
                    !user.isForcePasswordReset()) {

                // 1. Set the flag
                user.setForcePasswordReset(true);
                repository.save(user); // Save changes
                try {
                    // Load HTML template
                    String html = loadHtmlTemplate("templates/Reset-Password.html");
                    // Personalize
                    html = html.replace("{{USERNAME}}", user.getName());
                    // Send
                    emailService.sendHtmlEmail(user.getEmail(), "Password Expired", html);
                } catch (Exception e) {
                    System.err.println("Failed to send reminder to " + user.getEmail());
                    e.printStackTrace();
                }
            }

        }
    }







    private String loadHtmlTemplate(String path) throws Exception {
        ClassPathResource resource = new ClassPathResource(path);
        return new String(Files.readAllBytes(resource.getFile().toPath()));
    }



    public List<UserInfo> getAllUsers() {
        return repository.findAll();
    }
    /*delete a user by id*/

    public void deleteUserById(int id) {
        repository.deleteById(id);
    }



    public String addUser(UserInfo userInfo) {
        Optional<UserInfo> existingUser = repository.findByEmail(userInfo.getEmail());
        if (existingUser.isPresent()) {
            return "User already exists";
        }

        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        userInfo.setEnabled(false); // Mark not enabled

        // Generate token
        String token = UUID.randomUUID().toString();
        userInfo.setVerificationToken(token);
        userInfo.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));
        //set the lastpasswordchange to now
        userInfo.setLastPasswordChange(LocalDateTime.now());
        repository.save(userInfo);

        String verificationLink = "http://localhost:8089/auth/verify?token=" + token;
        emailService.sendVerificationEmail(userInfo.getEmail(), verificationLink);



        return "Verification email sent. Please check your inbox.";
    }




    public String addUserToClub(UserInfo userInfo, Long clubId) {
        // Check if user already exists
        Optional<UserInfo> existingUser = repository.findByEmail(userInfo.getEmail());
        if (existingUser.isPresent()) {
            return "User with this email already exists";
        }

        // Fetch the club
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club not found with id: " + clubId));

        // Store the raw password before encoding it
        String rawPassword = userInfo.getPassword();

        // Encode and set user data
        userInfo.setPassword(encoder.encode(rawPassword));
        userInfo.setEnabled(true);
        userInfo.setVerificationToken(null);
        userInfo.setVerificationTokenExpiry(null);
        userInfo.setLastPasswordChange(LocalDateTime.now());

        // Save the user
        UserInfo savedUser = repository.save(userInfo);

        // 👇 Ensure the club is tracking its coaches (update if you’ve refactored this relation)
        if (club.getCoaches() == null) {
            club.setCoaches(new HashSet<>());
        }
        club.getCoaches().add(savedUser);
        clubRepository.save(club);

        // Send welcome email
        try {
            String html = loadHtmlTemplate("templates/coach-welcome-email.html");

            html = html.replace("{{USERNAME}}", savedUser.getName())
                    .replace("{{CLUB_NAME}}", club.getName())
                    .replace("{{EMAIL}}", savedUser.getEmail())
                    .replace("{{PASSWORD}}", rawPassword);

            emailService.sendHtmlEmail(savedUser.getEmail(), "Welcome to the Club!", html);
        } catch (Exception e) {
            System.err.println("Failed to send welcome email to " + savedUser.getEmail());
            e.printStackTrace();
        }

        return "Coach added to club and notification email sent.";
    }



    public String verifyUser(String token) {
        Optional<UserInfo> optionalUser = repository.findByVerificationToken(token);

        if (optionalUser.isEmpty()) {
            return "Invalid verification token";
        }

        UserInfo user = optionalUser.get();

        if (user.getVerificationTokenExpiry() != null &&
                user.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
            return "Verification token expired";
        }

        user.setEnabled(true);
        user.setVerificationToken(null); // Invalidate token
        user.setVerificationTokenExpiry(null);
        repository.save(user);



        return "Email verified successfully. You can now log in.";
    }




















    public Map<String, Object> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> profileData = new HashMap<>();

        if (authentication != null && authentication.getPrincipal() instanceof UserInfoDetails) {
            UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();
            Long userId = (long) userDetails.getId();
            String username = userDetails.getUsername();

            profileData.put("username", username);
            profileData.put("userId", userId);

            Optional<Club> optionalClub = clubRepository.findByOwner_Id(userId);
            if (optionalClub.isPresent()) {
                Club club = optionalClub.get();
                profileData.put("clubId", club.getId());
                profileData.put("clubName", club.getName());
            } else {
                profileData.put("clubId", null);
                profileData.put("clubName", null);
            }

            return profileData;
        }

        return null;
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
        return repository.findAll().stream()
                .filter(user -> user.getRoles().contains("ROLE_USER") || user.getRoles().contains("ROLE_COACH"))
                .toList();
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



    public Map<String, Object> getBasicUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> profileData = new HashMap<>();

        if (authentication != null && authentication.getPrincipal() instanceof UserInfoDetails) {
            UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();

            profileData.put("email", userDetails.getUsername());
            profileData.put("role", userDetails.getAuthorities()); // or getRole(), depending on your implementation

            return profileData;
        }

        return null;
    }




    public String initiatePasswordReset(String email) {
        Optional<UserInfo> userOpt = repository.findByEmail(email);
        if (userOpt.isEmpty()) return "Email not found.";

        UserInfo user = userOpt.get();
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiryTime(LocalDateTime.now().plusHours(1));
        repository.save(user);

        // ✅ Send email with token
        String resetLink = "http://localhost:4200/reset-password?token=" + token;
        emailService.sendEmail(user.getEmail(),resetLink);

        return "Reset password link sent!";
    }




    public String resetPassword(String token, String newPassword) {
        Optional<UserInfo> optionalUser = repository.findByResetToken(token);
        if (optionalUser.isEmpty()) {
            return "Invalid token.";
        }

        UserInfo user = optionalUser.get();
        if (user.getTokenExpiryTime().isBefore(LocalDateTime.now())) {
            return "Token expired.";
        }

        user.setPassword(encoder.encode(newPassword));
        user.setResetToken(null);
        user.setTokenExpiryTime(null);
        //set the lastpasswordchange to now
        user.setLastPasswordChange(LocalDateTime.now());
        user.setForcePasswordReset(false);
        repository.save(user);
        return "Password updated successfully.";
    }



}
