package com.example.pi.service.trainingSessionServices;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {
    private Integer id;
    private Integer senderId;
    private Integer recipientId;
    private String content;
}