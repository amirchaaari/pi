package com.example.pi.controller;

import com.example.pi.entity.AuthRequest;
import com.example.pi.entity.Status;
import com.example.pi.entity.UserInfo;
import com.example.pi.service.JwtService;
import com.example.pi.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }
    @GetMapping("/user/userProfile")
    //@PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return service.getUserProfile();

    }
    @GetMapping("/userDetails")
    public ResponseEntity<UserInfo> getCurrentUser() {
        try {
            UserInfo user = service.getUser(); // Appelle ta méthode service.getUser()
            return ResponseEntity.ok(user);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 si non authentifié
        }
    }

    @GetMapping("/coach/coachProfile")
    //@PreAuthorize("hasAuthority('ROLE_COACH')")
    public String coachProfile() {
        return "Welcome to Coach Profile";
    }

    /*delete a user*/
    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable int id) {
        service.deleteUserById(id);
        return "User Deleted Successfully";
    }
    // Get list of coaches (for current user)
    @GetMapping("/coaches")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<UserInfo> getAllCoaches() {
        return service.getUsersByRole("ROLE_COACH");
    }

    // Get list of users (for coaches)
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_COACH')")
    public List<UserInfo> getAllUsers() {
        return service.getUsersByRole("ROLE_USER");
    }



    @GetMapping("/nutritionist/nutritionistProfile")
    //@PreAuthorize("hasAuthority('ROLE_NUTRITIONIST')")
    public String nutritionistProfile() {
        return "Welcome to Nutritionist Profile";
    }
    @GetMapping("/admin/adminProfile")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            service.updateStatus(authRequest.getUsername(), Status.ONLINE);
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsername(token.substring(7));
        service.updateStatus(email, Status.OFFLINE); // 👈 Set to OFFLINE
        return "User status set to OFFLINE";
    }


}
