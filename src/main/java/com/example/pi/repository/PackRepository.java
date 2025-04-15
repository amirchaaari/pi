package com.example.pi.repository;

import com.example.pi.entity.Pack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackRepository extends JpaRepository<Pack, Long> {
    @Query("SELECT p FROM Pack p LEFT JOIN p.abonnements a GROUP BY p.id ORDER BY COUNT(a) ASC")
    List<Pack> findAllOrderByPopularityAsc();

    List<Pack> findByClubId(Long clubId);
}
