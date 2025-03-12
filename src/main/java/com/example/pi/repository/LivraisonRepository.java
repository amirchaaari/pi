package com.example.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pi.entity.Livraison;

import java.util.List;

public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
            Livraison findByIdLivraison(int IdLivraison);
            void deleteByIdLivraison(int IdLivraison );
            List <Livraison> findByStatus (String status);


}
