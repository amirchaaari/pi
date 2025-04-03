package com.example.pi.service.trainingSessionServices;

import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    public void sendApprovalNotification(UserInfo user, TrainingSession session) {
        // Implement your notification logic (email, push, etc.)
        System.out.println("Notification sent to " + user.getEmail() +
                ": Your booking for session on " + session.getDate() + " was approved!");
    }

    public void notifyUser(UserInfo user, String s) {
        // Implement your notification logic (email, push, etc.)
        System.out.println("Notification sent to " + user.getEmail() + ": " + s);
    }
}