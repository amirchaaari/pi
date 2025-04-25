package com.example.pi.serviceInterface;

import com.example.pi.entity.Livraison;

import java.util.List;

public interface ILivraisonInterface {

    List<Livraison> GetListLivraisons();
    Livraison addLivraison(Livraison l);
    Livraison updateLivraison(Livraison l);

    Livraison retrieveLivraison(int idLivraison);


    void removeLivraison(int IdLivraison);
    List<Object[]> getLivraisonStatisticsByStatus();

}
