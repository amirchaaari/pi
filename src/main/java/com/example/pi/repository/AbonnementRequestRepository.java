package com.example.pi.repository;

import com.example.pi.entity.AbonnementRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbonnementRequestRepository extends JpaRepository<AbonnementRequest, Long> {
    List<AbonnementRequest> findByPack_Club_Owner_Id(Integer ownerId);
}
