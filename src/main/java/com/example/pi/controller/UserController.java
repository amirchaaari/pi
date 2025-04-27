package com.example.pi.controller;

import com.example.pi.entity.AuthRequest;
import com.example.pi.entity.Coach;
import com.example.pi.entity.Nutritionist;
import com.example.pi.entity.UserInfo;
import com.example.pi.service.JwtService;
import com.example.pi.service.UserInfoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody @Valid UserInfo userInfo) {
        return service.addUser(userInfo);
    }
    @PostMapping("/owner/add-to-club")


    public String addUserToClub(@RequestBody  UserInfo userInfo, @RequestParam Long clubId) {
        return service.addUserToClub(userInfo, clubId);
    }

    @GetMapping("/owner/userProfile")
    public ResponseEntity<Map<String, Object>> userProfile() {
        Map<String, Object> profile = service.getUserProfile();
        if (profile != null) {
            return ResponseEntity.ok(profile);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not authenticated"));
        }
    }






//    @GetMapping("/owner/ownerProfile")
//    public String ownerProfile() {
//        return service.getUserProfile();
//
//    }

    @GetMapping("/userProfile")
    public ResponseEntity<Map<String, Object>> coachProfile() {
        Map<String, Object> profile = service.getBasicUserProfile();
        if (profile != null) {
            return ResponseEntity.ok(profile);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not authenticated"));
        }
    }

    /*delete a user*/
    @DeleteMapping("/admin/deleteUser/{id}")
    public String deleteUser(@PathVariable int id) {
        service.deleteUserById(id);
        return "User Deleted Successfully";
    }


    @GetMapping("/nutritionist/nutritionistProfile")
    public String nutritionistProfile() {
        return "Welcome to Nutritionist Profile";
    }
    @GetMapping("/admin/adminProfile")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("/generateToken")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody  AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            if (authentication.isAuthenticated()) {
                UserInfo user = service.getUserByEmail(authRequest.getUsername());

                if (user.isForcePasswordReset()) {
                    service.initiatePasswordReset(user.getEmail());

                    // Return 403 Forbidden with a message
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Collections.singletonMap("message", "Password expired. A reset link has been sent to your email."));
                }

                service.incrementSessionsAndSaveIt(authRequest.getUsername());
                String token = jwtService.generateToken(authRequest.getUsername());
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "Invalid user request!"));
            }

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Authentication failed. Check your credentials."));
        }
    }


//    @PutMapping("/admin/blockUser/{id}")
//    public String blockOrUnblockUser(@PathVariable int id, @RequestParam boolean block) {
//        return service.toggleUserBlockStatus(id, block);
//    }









    @GetMapping("/verify")
    public String verifyUser(@RequestParam("token") String token) {
        return service.verifyUser(token);
    }



    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        return service.initiatePasswordReset(email);
    }



    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String newPassword = payload.get("newPassword");
        return service.resetPassword(token, newPassword);
    }

    @GetMapping("/admin/getAllUsers")
    public List<UserInfo> getAllUsers() {
        return service.getAllUsers();
    }






}
