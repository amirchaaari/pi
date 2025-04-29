package com.example.pi.service.serviceimpProducts;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendGlobalNotification(String message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }

    public void sendLowStockNotification(String productName, int remainingQuantity) {
        String message = "Low stock alert: " + productName + " (" + remainingQuantity + " left)";
        sendGlobalNotification(message);
    }
}
