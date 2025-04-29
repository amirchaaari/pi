package com.example.pi.repository;

import com.example.pi.entity.Command;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandRepository extends JpaRepository<Command, Long> {
    // Additional query methods can be defined here if needed

    List<Command> findByUserId(Long userId);
    List<Command> findByProductId(Long productId);

    @Query("SELECT c FROM Command c JOIN FETCH c.product WHERE c.user.id = :userId")
    List<Command> findCommandsWithProductsByUserId(@Param("userId") Long userId);
}