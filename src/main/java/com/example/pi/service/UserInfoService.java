package com.example.pi.service;


import com.example.pi.entity.UserInfo;
import com.example.pi.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserInfoService implements UserDetailsService {
    @Autowired
    private EmailService emailService;


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

//    public String addUser(UserInfo userInfo) {
//        // Encode password before saving the user
//        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
//        repository.save(userInfo);
//        return "User Added Successfully";
//    }

//    public String addUser(UserInfo userInfo) {
//        Optional<UserInfo> existingUser = repository.findByEmail(userInfo.getEmail());
//        if (existingUser.isPresent()) {
//            return "User already  exists";
//
//
//        }
//
//
//
//        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
//        repository.save(userInfo);
//        return "User Added Successfully";
//    }


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

        repository.save(userInfo);

        String verificationLink = "http://localhost:8089/auth/verify?token=" + token;
        emailService.sendVerificationEmail(userInfo.getEmail(), verificationLink);

        return "Verification email sent. Please check your inbox.";
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








    public String getUserProfile() {
        // Get authentication from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserInfoDetails) {
            UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();
            return "Welcome " + userDetails.getUsername() + "your id is " + userDetails.getId();
        }

        return "User not authenticated";
    }




}