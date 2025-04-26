package com.example.pi.service.maps;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DirectionsResult;  // Add this import
import com.google.maps.model.DirectionsRoute;  // Add this import
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.TravelMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


@Service
public class MapsService {

    private final GeoApiContext context;

    // Correct constructor (no return type!)
    @Autowired  // Explicitly mark for injection (optional but recommended)
    public MapsService(GeoApiContext context) {
        this.context = context;
    }


    public GeocodingResult[] geocodeAddress(String address) throws Exception {
        return GeocodingApi.geocode(context, address).await();
    }



    public long getDuration(String origin, String destination, TravelMode mode) throws Exception {
        DirectionsResult result = DirectionsApi.newRequest(context)
                .origin(origin)
                .destination(destination)
                .mode(mode) // Can be DRIVING, WALKING, BICYCLING, or TRANSIT
                .await();

        if (result.routes != null && result.routes.length > 0) {
            DirectionsRoute route = result.routes[0];
            if (route.legs != null && route.legs.length > 0) {
                // Duration in seconds
                return route.legs[0].duration.inSeconds;
            }
        }
        throw new RuntimeException("No route found between " + origin + " and " + destination);
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
}
