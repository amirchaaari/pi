package com.example.pi.controller.Livraison;

import com.example.pi.entity.Livreur;
import com.example.pi.service.Maps.MapsService;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.TravelMode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/Maps")
public class MapsController {

    private final MapsService googleMapsService;

    public MapsController(MapsService MapsService) {
        this.googleMapsService = MapsService;
    }

    @GetMapping("/geocode")
    public GeocodingResult[] geocodeAddress(@RequestParam String address) throws Exception {
        return googleMapsService.geocodeAddress(address);
    }

    @GetMapping("/duration")
    public ResponseEntity<Long> getDuration(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam(defaultValue = "DRIVING") TravelMode mode) {
        try {
            long durationInSeconds = googleMapsService.getDuration(origin, destination, mode);
            return ResponseEntity.ok(durationInSeconds);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }


    @GetMapping("/nearest-driver")
    public ResponseEntity<?> findNearestDriver(@RequestParam String deliveryAddress) {
        try {
            // Preserve plus codes by re-encoding just the plus sign
            String processedAddress = deliveryAddress.replace("+", "%2B");

            Optional<Livreur> nearestDriver = googleMapsService.findNearestAvailableDriver(processedAddress);
            return nearestDriver
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }}
}