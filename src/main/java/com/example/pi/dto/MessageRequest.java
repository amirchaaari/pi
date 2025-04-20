package com.example.pi.dto;

import lombok.Data;

@Data
public class MessageRequest {
    private Integer senderId;
    private Integer receiverId;
    private String content;
}