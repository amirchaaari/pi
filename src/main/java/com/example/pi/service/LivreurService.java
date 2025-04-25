package com.example.pi.service;

import com.example.pi.entity.Livreur;
import com.example.pi.repository.LivreurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



import java.util.List;
@Service
@AllArgsConstructor

public class LivreurService {


    private LivreurRepository livreurRepository;
    private EmailService emailService;

    // Register a new driver
    public Livreur registerDriver(Livreur livreur) {
        return livreurRepository.save(livreur);
    }

    // Update driver availability
    public Livreur updateAvailability(Long driverId, boolean available) {
        Livreur driver = livreurRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        driver.setAvailable(available);
        return livreurRepository.save(driver);
    }

    // Get all available drivers
    public List<Livreur> getAvailableDrivers() {
        return livreurRepository.findByAvailableTrue();
    }

    // Notify driver about a delivery (without linking entities)
    public void notifyDriver(Long driverId, String deliveryAddress, String qrCodeUrl) {
        Livreur driver = livreurRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        emailService.sendDriverAssignment(
                driver.getEmail(),
                deliveryAddress

        );
    }
}
