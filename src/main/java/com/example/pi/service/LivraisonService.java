
package com.example.pi.service;

import com.example.pi.entity.Command;
import com.example.pi.entity.Product;
import com.example.pi.entity.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.pi.entity.Livraison;
import com.example.pi.repository.LivraisonRepository;
import com.example.pi.serviceInterface.ILivraisonInterface;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LivraisonService implements ILivraisonInterface {

    LivraisonRepository livraisonRepository;



    @Override
    public List<Livraison> GetListLivraisons() {
        return livraisonRepository.findAll();
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


    public Livraison scheduleDelivery(Long livraisonId, Date scheduledDate) {
        Optional<Livraison> livraisonOpt = livraisonRepository.findById(livraisonId);

        if (livraisonOpt.isPresent()) {
            Livraison livraison = livraisonOpt.get();
            livraison.setScheduleddate(scheduledDate);
            livraisonRepository.save(livraison);
        }

        return livraisonOpt.orElse(null);
    }


    public void confirmLivraison(Long livraisonId) {
        Optional<Livraison> livraisonOpt = livraisonRepository.findById(livraisonId);

        if (livraisonOpt.isPresent()) {
            Livraison livraison = livraisonOpt.get();
            livraison.setStatus(Livraison.DeliveryStatus.DELIVERED);
            livraisonRepository.save(livraison);
        }
    }


    /*@Scheduled(cron = "0 0 * * * ?")
    public void alertForArrivingLivraisons() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime alertTime = now.plusHours(2);
        Date alertDate = Date.from(alertTime.atZone(ZoneId.systemDefault()).toInstant());

        List<Livraison> arrivingSoonLivraisons = livraisonRepository.findAll().stream()
                .filter(livraison -> livraison.getScheduleddate() != null &&
                        livraison.getScheduleddate().after(new Date()) &&
                        livraison.getScheduleddate().before(alertDate))
                .toList();

        for (Livraison livraison : arrivingSoonLivraisons) {
            System.out.println("ALERT: Livraison with ID " + livraison.getIdLivraison() +
                    " is arriving soon at " + livraison.getScheduleddate());

        }
    }*/


    public List<Object[]> getLivraisonStatisticsByStatus() {
        return livraisonRepository.countLivraisonsByStatus();
    }






}

