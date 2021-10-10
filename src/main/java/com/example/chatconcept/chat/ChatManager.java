package com.example.chatconcept.chat;

import com.example.chatconcept.UnknownChatroomException;
import com.example.chatconcept.ws.WebSocketManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class ChatManager {

    private final ChatroomRepository chatroomRepository;
    private final ChatRepository chatRepository;
    private final WebSocketManager webSocketManager;
    private final Clock clock;

    public void sendChat(Chat chat) {
        Chatroom chatroom = chatroomRepository.get(chat.getRoomId())
                        .orElseThrow(() -> new UnknownChatroomException("No chatroom found for given id"));
        chatRepository.save(chat);
        chatroomRepository.insert(chatroom.withUpdated(clock.instant()));
        chatroom.getMembers()
                .stream()
                .filter(webSocketManager::userConnected)
                .forEach(userId -> webSocketManager.sendMessage(userId, chatroom.getId().toString()));
    }

    public List<Chat> getChatsAfter(UUID room, Instant after) {
        return chatRepository.getAfter(room, after);
    }

    public List<Chat> getRecentChats(UUID room, int count) {
        return chatRepository.getRecent(room, count);
    }

    public Collection<Chatroom> userChatrooms(UUID userId) {
        return chatroomRepository.getAll(userId);
    }

    public Optional<Chatroom> getChatroom(UUID roomId) {
        return chatroomRepository.get(roomId);
    }

    public Collection<Chatroom> getAll() {
        return chatroomRepository.allChatrooms();
    }

    public boolean hasChatroom(UUID userId, UUID roomId) {
        return userChatrooms(userId)
                .stream()
                .anyMatch(chatroom -> chatroom.getId().equals(roomId));
    }
}
