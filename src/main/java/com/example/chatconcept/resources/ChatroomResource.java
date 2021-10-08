package com.example.chatconcept.resources;

import com.example.chatconcept.chat.ChatManager;
import com.example.chatconcept.chat.Chatroom;
import com.example.chatconcept.chat.ChatroomRepository;
import com.example.chatconcept.UnknownChatroomException;
import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ChatroomResource {

    private final ChatManager chatManager;
    private final ChatroomRepository chatroomRepository;
    private final Clock clock;

    @GetMapping(value = "/chatroom/{roomId}")
    public Chatroom getChatroom(@PathVariable UUID roomId, @UserId UUID userId) {
        if (chatManager.hasChatroom(userId, roomId)) {
            return chatManager.getChatroom(roomId)
                    .orElseThrow(() -> new UnknownChatroomException("No chatroom found for given id"));
        } else {
            throw new UnknownChatroomException("User not in given chatroom");
        }
    }

    // temporary, use one shared chatroom for everything
    @PostMapping(value = "/chatroom/join")
    public UUID joinChatroom(@UserId UUID userId) {
        UUID roomId = chatManager.getAll()
                .stream()
                .findAny()
                .map(Chatroom::getId)
                .orElseGet(() -> {
                    UUID id = UUID.randomUUID();
                    chatroomRepository.insert(new Chatroom(id, ImmutableList.of(), clock.instant(), false));
                    return id;
                });
        chatroomRepository.addMember(roomId, userId);
        return roomId;
    }
}
