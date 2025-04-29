package com.example.pi.controller.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class WebSocketTestController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/trigger-notification")
    public String sendTestNotification() {
        String message = "Backend test notification at " + LocalDateTime.now();
        messagingTemplate.convertAndSend("/topic/notifications",
                Map.of("content", message, "timestamp", LocalDateTime.now()));
        return "Notification sent!";
    }
}