package com.example.pi.repository;

import com.example.pi.entity.Command;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRepository extends JpaRepository<Command, Long> {
    // Additional query methods can be defined here if needed
}