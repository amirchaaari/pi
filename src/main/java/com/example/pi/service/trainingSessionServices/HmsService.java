package com.example.pi.service.trainingSessionServices;

import com.example.pi.config.HmsConfig;
import com.example.pi.entity.TrainingSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HmsService {
    private final HmsConfig hmsConfig;
    private final RestTemplate restTemplate;
    private static final String BASE_URL = "https://api.100ms.live/v2/room-codes/room/";
    private static final String BASE_MEETING_URL = "https://lukatn-videoconf-1306.app.100ms.live/meeting/";


    public String createRoom(TrainingSession session) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(hmsConfig.getManagementKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("description", session.getDescription());
        requestBody.put("template_id", hmsConfig.getTemplateId());
        requestBody.put("region", "in");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.100ms.live/v2/rooms",
                request,
                Map.class
        );
        System.out.println(response.getBody());
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return (String) response.getBody().get("id");
        }
        throw new RuntimeException("Failed to create 100ms room");
    }
    public String generateMeetingLink(String roomId) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + hmsConfig.getManagementKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                BASE_URL + roomId,
                HttpMethod.GET,
                entity,
                String.class
        );

//        if (response.getStatusCode() == HttpStatus.OK) {
//            JSONObject json = new JSONObject(response.getBody());
//            JSONArray dataArray = json.getJSONArray("data");
//
//            for (int i = 0; i < dataArray.length(); i++) {
//                JSONObject obj = dataArray.getJSONObject(i);
//                if ("host".equalsIgnoreCase(obj.getString("role"))) {
//                    String code = obj.getString("code");
//                    return BASE_MEETING_URL + code;
//                }
//            }
//        }

        return null; // or throw an error/exception
    }



}