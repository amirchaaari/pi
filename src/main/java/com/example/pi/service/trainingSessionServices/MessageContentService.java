package com.example.pi.service.trainingSessionServices;

import com.example.pi.entity.MessageContent;
import com.example.pi.entity.MessageRoom;
import com.example.pi.entity.UserInfo;
import com.example.pi.repository.trainignSessionRepo.MessageContentRepository;
import com.example.pi.service.UserInfoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageContentService {
    @Autowired
    private final MessageContentRepository messageContentRepository;
    @Autowired
    private final MessageRoomService messageRoomService;
    @Autowired
    private final UserInfoService userService;



    public MessageContent sendMessage(Integer senderId, Integer receiverId, String content) {
        MessageRoom room = messageRoomService.getOrCreateRoom(senderId, receiverId);
        UserInfo sender = userService.getUserById(senderId);

        MessageContent message = new MessageContent();
        message.setContent(content);
        message.setSender(sender);
        message.setTimestamp(LocalDateTime.now());
        message.setMessageRoom(room);

        return messageContentRepository.save(message);
    }

    public List<MessageContent> getChatHistory(Integer user1Id, Integer user2Id) {
        MessageRoom room = messageRoomService.getOrCreateRoom(user1Id, user2Id);
        return messageContentRepository.findAllMessagesByRoomId(room.getId());
    }
}
