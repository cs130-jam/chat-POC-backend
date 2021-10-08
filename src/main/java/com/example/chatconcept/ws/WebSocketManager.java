package com.example.chatconcept.ws;

import com.example.chatconcept.UnknownUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.UUID;

@RequiredArgsConstructor
public class WebSocketManager {

    private final PrincipalRepository principalRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendMessage(UUID userId, String destination, Object message) {
        Principal principal = principalRepository.getByUser(userId)
                .orElseThrow(() -> new UnknownUserException("No principal found for given user id"));
        simpMessagingTemplate.convertAndSendToUser(principal.getName(), destination, message);
    }
}
