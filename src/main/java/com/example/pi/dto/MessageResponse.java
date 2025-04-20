package com.example.pi.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageResponse {
    private UUID id;
    private String content;
    private Integer senderId;
    private LocalDateTime timestamp;
    private String uniqueRoomId;
}