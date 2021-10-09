package com.example.chatconcept.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.example.chatconcept.UnknownChatroomException;
import com.example.chatconcept.UserId;
import com.example.chatconcept.chat.Chat;
import com.example.chatconcept.chat.ChatManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ChatResource {

    private final ObjectMapper objectMapper;
    private final ChatManager chatManager;
    private final Clock clock;

    @SneakyThrows
    @GetMapping(value = "/chatroom/{roomId}/after", produces = APPLICATION_JSON_VALUE)
    public List<Chat> chatsAfter(@PathVariable UUID roomId, @RequestParam String time, @UserId UUID userId) {
        Instant timestamp = objectMapper.readerFor(Instant.class).readValue(time);
        if (chatManager.hasChatroom(userId, roomId)) {
            return chatManager.getChatsAfter(roomId, timestamp);
        } else {
            throw new UnknownChatroomException("User not in given chatroom");
        }
    }

    @GetMapping(value = "/chatroom/{roomId}/recent", produces = APPLICATION_JSON_VALUE)
    public List<Chat> recentChats(@PathVariable UUID roomId, @RequestParam int count, @UserId UUID userId) {
        if (chatManager.hasChatroom(userId, roomId)) {
            return chatManager.getRecentChats(roomId, count);
        } else {
            throw new UnknownChatroomException("User not in given chatroom");
        }
    }

    @PostMapping(value = "/chatroom/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    public void sendChat(@PathVariable UUID roomId, @RequestBody String chat, @UserId UUID userId) {
        chatManager.sendChat(new Chat(null, roomId, userId, chat, clock.instant()));
    }
}
