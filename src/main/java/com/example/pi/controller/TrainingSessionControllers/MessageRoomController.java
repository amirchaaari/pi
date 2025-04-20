package com.example.pi.controller.TrainingSessionControllers;

import com.example.pi.entity.MessageRoom;
import com.example.pi.service.trainingSessionServices.MessageRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/messagerooms")
public class MessageRoomController {

    private final MessageRoomService messageRoomService;

    @PostMapping("/create-chat-room")
    public ResponseEntity<MessageRoom> create(@RequestParam final Integer user1Id,
                                                 @RequestParam final Integer user2Id) {
        return ResponseEntity.ok(messageRoomService.getOrCreateRoom(user1Id, user2Id));
    }


}
