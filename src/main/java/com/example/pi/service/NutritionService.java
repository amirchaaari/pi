package com.example.pi.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NutritionService {
    private final String apiKey = "3b2329d1785d4e4b9b103cd5580f67ba";
    private final String apiUrl = "https://api.spoonacular.com/mealplanner/generate";

    public String getNutritionRecommendation(String targetGoal) {
        RestTemplate restTemplate = new RestTemplate();

        String fullUrl = apiUrl + "?timeFrame=day&diet=" + targetGoal + "&apiKey=" + apiKey;

        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);

        return response.getBody();
    }
}
