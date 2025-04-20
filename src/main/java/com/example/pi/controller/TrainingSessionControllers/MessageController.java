package com.example.pi.controller.TrainingSessionControllers;

import com.example.pi.dto.MessageRequest;
import com.example.pi.dto.MessageResponse;
import com.example.pi.entity.MessageContent;
import com.example.pi.service.trainingSessionServices.MessageContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageContentService messageContentService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send-message")
    public void handleMessage(MessageRequest request) {
        MessageContent message = messageContentService.sendMessage(
                request.getSenderId(),
                request.getReceiverId(),
                request.getContent()
        );
        MessageResponse response = convertToResponse(message);
        String roomId = message.getMessageRoom().getUniqueRoomId();
        messagingTemplate.convertAndSend("/topic/room/" + roomId, response);
    }

    private MessageResponse convertToResponse(MessageContent message) {
        MessageResponse response = new MessageResponse();
        response.setId(message.getId());
        response.setContent(message.getContent());
        response.setSenderId(message.getSender().getId());
        response.setTimestamp(message.getTimestamp());
        response.setUniqueRoomId(message.getMessageRoom().getUniqueRoomId());
        return response;
    }
}