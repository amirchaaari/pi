package com.example.pi.service.Maps;

import com.example.pi.entity.Livreur;
import com.example.pi.repository.LivraisonRepository.LivreurRepository;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.TravelMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


@Service
public class MapsService {

    private final GeoApiContext context;
    private final LivreurRepository livreurRepository;  // Make final for immutability

    @Autowired
    public MapsService(GeoApiContext context, LivreurRepository livreurRepository) {
        this.context = context;
        this.livreurRepository = livreurRepository;
    }


    public GeocodingResult[] geocodeAddress(String address) throws Exception {
        return GeocodingApi.geocode(context, address).await();
    }


    public long getDuration(String origin, String destination, TravelMode mode) throws Exception {
        // Create new context for each request
        try (GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyDODHGmGhg5lsUhq-5tM5nZ_xckcyFDw9Q")
                .build()) {

            DirectionsResult result = DirectionsApi.newRequest(context)
                    .origin(origin)
                    .destination(destination)
                    .mode(mode)
                    .await();

            if (result.routes == null || result.routes.length == 0) {
                throw new RuntimeException("No routes found");
            }
            return result.routes[0].legs[0].duration.inSeconds;
        }
    }


    // Convert duration (seconds) to a Date (for your Livraison entity)
    public Date convertSecondsToDate(long durationInSeconds) {
        Instant instant = Instant.now().plusSeconds(durationInSeconds);
        return Date.from(instant);
    }

    // Alternative: Convert to LocalDate (if needed)
    public LocalDate convertSecondsToLocalDate(long durationInSeconds) {
        Instant instant = Instant.now().plusSeconds(durationInSeconds);
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public Optional<Livreur> findNearestAvailableDriver(String deliveryAddress) throws Exception {
        List<Livreur> availableDrivers = livreurRepository.findByAvailableTrue();
        Map<Livreur, Long> validDrivers = new HashMap<>();

        for (Livreur driver : availableDrivers) {
            try {
                // Use raw addresses without additional encoding
                long duration = this.getDuration(
                        driver.getAddress(),  // No encoding
                        deliveryAddress,      // Already processed
                        TravelMode.DRIVING
                );
                validDrivers.put(driver, duration);
            } catch (Exception e) {

            }
        }

        return validDrivers.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    private void validateAddress(String address) throws IllegalArgumentException {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
        if (address.length() < 5) {
            throw new IllegalArgumentException("Address is too short");
        }
    }
}
