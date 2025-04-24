package com.example.pi.controller.TrainingSessionControllers;

import com.example.pi.entity.ChatMessage;
import com.example.pi.entity.UserInfo;
import com.example.pi.service.UserInfoService;
import com.example.pi.service.trainingSessionServices.ChatMessageService;
import com.example.pi.service.trainingSessionServices.ChatNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/training-sessions")
public class ChatController {
    private final UserInfoService userService;

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId().toString(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
    }

/*    @PostMapping("/chat")
    public ResponseEntity<ChatMessage> saveMessage(@RequestBody ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        return ResponseEntity.ok(savedMsg);
    }*/


    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable Integer senderId,
                                                              @PathVariable Integer recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
    @GetMapping("/users")
    public ResponseEntity<List<UserInfo>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }
}
