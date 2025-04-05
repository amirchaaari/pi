package com.example.pi.repository;

import com.example.pi.entity.DietProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface DietProgramRepo extends CrudRepository<DietProgram, Long> {
}
