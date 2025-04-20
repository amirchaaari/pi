package com.example.pi.service;


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
}
