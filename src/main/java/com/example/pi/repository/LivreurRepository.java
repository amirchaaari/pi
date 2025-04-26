package com.example.pi.repository;

import com.example.pi.entity.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LivreurRepository extends JpaRepository<Livreur, Long> {

    List<Livreur> findByAvailableTrue();

    @Query("SELECT l FROM Livreur l WHERE l.available = true ORDER BY FUNCTION('ST_Distance', l.address, :address)")
    List<Livreur> findAvailableNearAddress(@Param("address") String address);

    Optional<Livreur> findByEmail(String email);
}
