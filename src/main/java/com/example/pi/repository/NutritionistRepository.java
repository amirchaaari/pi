package com.example.pi.repository;

import com.example.pi.entity.Nutritionist;
import com.example.pi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NutritionistRepository extends JpaRepository<Nutritionist, Integer> {
    Optional<UserInfo> findByEmail(String email); // Use 'email' if that is the correct field for login

}
