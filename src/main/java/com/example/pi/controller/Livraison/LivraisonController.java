package com.example.pi.controller.Livraison;

import com.example.pi.entity.Livraison;
import com.example.pi.service.LivraisonService.LivraisonService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/Livraison")
public class LivraisonController {
        LivraisonService livraisonService;
    @GetMapping("/retrieve-all-livraison")
    public List<Livraison> getAllLivraison() {
        return livraisonService.GetListLivraisons();
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
   @GetMapping("/statistics")
public List<Object[]> getLivraisonStatistics() {
    return livraisonService.getLivraisonStatisticsByStatus();
}
}
