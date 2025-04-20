package com.example.pi.service.trainingSessionServices;

import com.example.pi.entity.MessageContent;
import com.example.pi.entity.MessageRoom;
import com.example.pi.entity.UserInfo;
import com.example.pi.repository.trainignSessionRepo.MessageRoomRepository;
import com.example.pi.service.UserInfoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageRoomService {

    private final MessageRoomRepository messageRoomRepository;
    private final UserInfoService userService;


    public MessageRoom getOrCreateRoom(Integer user1Id, Integer user2Id) {
        // Check if room already exists
        Optional<MessageRoom> existingRoom =
                messageRoomRepository.findRoomByUserIds(user1Id, user2Id);

        if (existingRoom.isPresent()) {
            return existingRoom.get();
        }

        // Create new room
        UserInfo user1 = userService.getUserById(user1Id);
        UserInfo user2 = userService.getUserById(user2Id);

        MessageRoom newRoom = new MessageRoom();
        newRoom.setUser1(user1);
        newRoom.setUser2(user2);
        newRoom.setUniqueRoomId(generateUniqueRoomId(user1Id, user2Id));
        newRoom.setCreatedAt(LocalDateTime.now());
        MessageContent messageContent = MessageContent.builder()
                .content("Hi")
                .timestamp(LocalDateTime.now())
                .messageRoom(newRoom)
                .sender(user1)
                .build();

        if(newRoom.getMessages() == null) {
            newRoom.setMessages(new ArrayList<>());
        }
        newRoom.getMessages().add(messageContent);
        return messageRoomRepository.save(newRoom);
    }

    private String generateUniqueRoomId(Integer id1, Integer id2) {
        return Stream.of(id1.toString(), id2.toString())
                .sorted()
                .collect(Collectors.joining("_"));
    }


}
