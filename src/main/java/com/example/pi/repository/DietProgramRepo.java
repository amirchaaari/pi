package com.example.pi.repository;

import com.example.pi.entity.DietProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DietProgramRepo extends CrudRepository<DietProgram, Long> {
    List<DietProgram> findByName(String name);  // Recherche par nom
}
