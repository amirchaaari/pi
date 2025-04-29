package com.example.pi.repository;

import com.example.pi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Additional query methods can be defined here if needed
    Optional<Category> findByName(String name);

}