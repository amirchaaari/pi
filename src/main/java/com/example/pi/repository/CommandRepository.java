package com.example.pi.repository;

import com.example.pi.entity.Command;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandRepository extends JpaRepository<Command, Long> {
    // Additional query methods can be defined here if needed

    List<Command> findByUserId(Long userId);
    List<Command> findByProductId(Long productId);
}