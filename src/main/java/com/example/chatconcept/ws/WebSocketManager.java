package com.example.chatconcept.ws;

import static com.example.chatconcept.LoginManager.SESSION_TOKEN_KEY;

import com.example.chatconcept.LoginManager;
import com.example.chatconcept.SessionToken;
import com.example.chatconcept.UnknownUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class WebSocketManager extends DefaultHandshakeHandler {

    private final PrincipalRepository principalRepository;
    private final LoginManager loginManager;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        principalRepository.insert(userIdFromAttributes(attributes), request.getPrincipal());
        return request.getPrincipal();
    }

    private UUID userIdFromAttributes(Map<String, Object> attributes) {
        return loginManager.userIdForToken(SessionToken.fromString(
                (String) Optional.ofNullable(attributes.get(SESSION_TOKEN_KEY))
                        .orElseThrow(() -> new UnknownUserException("attributes did not contain session token"))))
                .orElseThrow(() -> new UnknownUserException("No user found for given session token"));
    }

    public void sendMessage(UUID userId, String destination, Object message) {
        Principal principal = principalRepository.getByUser(userId)
                .orElseThrow(() -> new UnknownUserException("No principal found for given user id"));
        simpMessagingTemplate.convertAndSendToUser(principal.getName(), destination, message);
    }
}
