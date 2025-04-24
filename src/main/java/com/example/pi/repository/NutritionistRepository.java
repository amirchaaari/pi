package com.example.pi.repository;

import com.example.pi.entity.Nutritionist;
import com.example.pi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NutritionistRepository extends JpaRepository<Nutritionist, Long> {
}
