package com.example.pi.repository.LivraisonRepository;

import com.example.pi.entity.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
            Livraison findByIdLivraison(int IdLivraison);
            void deleteByIdLivraison(int IdLivraison );
            List <Livraison> findByStatus (String status);


    @Query("SELECT l.status, COUNT(l) FROM Livraison l GROUP BY l.status")
    List<Object[]> countLivraisonsByStatus();


}
