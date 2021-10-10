package com.example.chatconcept.ws;

import com.example.chatconcept.UnknownUserException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class WebSocketManager {

    private final InMemorySessionRepository sessionRepository;

    @SneakyThrows(IOException.class)
    public void sendMessage(UUID userId, String message) {
        sessionRepository.get(userId)
                .orElseThrow(() -> new UnknownUserException("No principal found for given user id"))
                .sendMessage(new TextMessage(message));
    }

    public boolean userConnected(UUID userId) {
        return sessionRepository.get(userId)
                .map(WebSocketSession::isOpen)
                .orElse(false);
    }
}
