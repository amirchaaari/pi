
package com.example.pi.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.pi.entity.Livraison;
import com.example.pi.repository.LivraisonRepository;
import com.example.pi.serviceInterface.ILivraisonInterface;

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

    /*public Command createCommand(Long productId, Integer quantity, Long userId) {
        // Find user and product
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        // Create and configure the delivery
        Livraison livraison = new Livraison();
        livraison.setAddress(user.getAddress()); // Assuming UserInfo has an address field
        livraison.setStatus(Livraison.DeliveryStatus.PENDING);

        // Set scheduled date to today + 3 days
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 3);
        livraison.setScheduleddate(calendar.getTime());

        // Set duration (you might want to calculate this differently)
        livraison.setDuration(calendar.getTime()); // Or set a specific duration

        // Create the command
        Command command = new Command();
        command.setUser(user);
        command.setProduct(product);
        command.setQuantity(quantity);
        command.setLivraison(livraison);

        // Save the delivery first (since it's the owning side of the relationship)
        livraison = livraisonRepository.save(livraison);

        // Then save the command
        command = commandRepository.save(command);

        // Add the command to the delivery's set of commands
        livraison.getCommands().add(command);
        livraisonRepository.save(livraison);

        return command;
    }

*/


}

