package com.example.pi.repository;


import com.example.pi.entity.Abonnement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
    List<Abonnement> findByUserId(int userId);

    List<Abonnement> findByPack_Club_Owner_Id(int id);

    @Query("SELECT a.pack.id, COUNT(a) FROM Abonnement a GROUP BY a.pack.id")
    List<Object[]> countAbonnementsByPack();

    List<Abonnement> findByPackClubId(Long clubId);
    @Query("SELECT COUNT(a) FROM Abonnement a WHERE a.pack.id = :packId")
    int countByPackId(@Param("packId") Long packId);

}