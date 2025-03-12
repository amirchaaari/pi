package com.example.pi.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.pi.entity.Livraison;
import com.example.pi.repository.LivraisonRepository;
import com.example.pi.serviceInterface.ILivraisonInterface;

import java.util.List;

@Service
@AllArgsConstructor
public class LivraisonService implements ILivraisonInterface {

    LivraisonRepository livraisonRepository;



    @Override
    public List<Livraison> GetListLivraisons(String status) {
        return livraisonRepository.findByStatus(status);
    }

    @Override
    public Livraison addLivraison(Livraison l) {
        livraisonRepository.save(l);
        return l;
    }

    @Override
    public Livraison updateLivraison(Livraison l) {
        return livraisonRepository.save(l);
    }

    @Override
    public Livraison retrieveLivraison(int idLivraison) {
        return livraisonRepository.findByIdLivraison(idLivraison);
    }

    @Override
    public void removeLivraison(int IdLivraison) {
        livraisonRepository.deleteById((long) IdLivraison);

    }




}
