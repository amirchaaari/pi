package com.example.pi.controller;

import com.example.pi.entity.AuthRequest;
import com.example.pi.entity.Coach;
import com.example.pi.entity.Nutritionist;
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
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
