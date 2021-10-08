package com.example.chatconcept.chat;

import com.example.chatconcept.ws.WebSocketContext;
import com.example.chatconcept.ws.WebSocketManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.time.Clock;

@Import({
        WebSocketContext.class,

        DbChatRepository.class,
        DbChatroomRepository.class
})
public class ChatContext {
    @Bean
    public ChatManager chatManager(
            ChatroomRepository chatroomRepository,
            ChatRepository chatRepository,
            WebSocketManager webSocketManager,
            Clock clock
    ) {
        return new ChatManager(chatroomRepository, chatRepository, webSocketManager, clock);
    }
}
