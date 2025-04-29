
package com.example.pi.service.LivraisonService;

import com.example.pi.entity.Livraison;
import com.example.pi.entity.Livreur;
import com.example.pi.repository.LivraisonRepository.LivraisonRepository;
import com.example.pi.repository.LivraisonRepository.LivreurRepository;
import com.example.pi.service.EmailService;
import com.example.pi.service.Maps.MapsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LivraisonService {
    private final LivreurRepository livreurRepository;
    private final EmailService emailService;
    private final MapsService mapsService;


    LivraisonRepository livraisonRepository;




    public List<Livraison> GetListLivraisons() {
        return livraisonRepository.findAll();
    }




    public Livraison addLivraison(Livraison l) {
        assignDelivery(l);
        livraisonRepository.save(l);
        return l;
    }


    public Livraison updateLivraison(Livraison l) {
        return livraisonRepository.save(l);
    }


    public Livraison retrieveLivraison(int idLivraison) {
        return livraisonRepository.findByIdLivraison(idLivraison);
    }


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


    public void assignDelivery(Livraison delivery) {
        try {
            // Encode the delivery address
            String encodedDeliveryAddress = URLEncoder.encode(delivery.getAddress(), StandardCharsets.UTF_8);
            Optional<Livreur> nearestDriver = mapsService.findNearestAvailableDriver(encodedDeliveryAddress);

            nearestDriver.ifPresent(driver -> {
                emailService.sendDriverAssignment(
                        driver.getEmail(),
                        delivery.getAddress(),
                        delivery.getIdLivraison()// Send original address for readability
                );
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to assign driver: " + e.getMessage());
        }}


}

