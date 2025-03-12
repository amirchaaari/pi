package com.example.pi.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.pi.entity.Livraison;
import com.example.pi.serviceInterface.ILivraisonInterface;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Livraison")
public class LivraisonController {
        ILivraisonInterface livraisonService;
    @GetMapping("/retrieve-all-livraison")
    public List<Livraison> getAllLivraison(String status) {
        return livraisonService.GetListLivraisons(status);
    }

    @PostMapping("/add-livraison")
    public Livraison addLivraison(@RequestBody Livraison l) {
        return livraisonService.addLivraison(l);
    }

    @DeleteMapping("/removeLivraison/{IdLivraison}")
    @ResponseBody
    public void removeLivraison(@PathVariable("IdLivraison") int IdLivraison ){
        livraisonService.removeLivraison(IdLivraison);
    }
}
