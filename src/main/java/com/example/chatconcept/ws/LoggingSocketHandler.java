package com.example.chatconcept.ws;

import static com.example.chatconcept.user.LoginManager.SESSION_TOKEN_KEY;

import com.example.chatconcept.UnknownTokenException;
import com.example.chatconcept.user.LoginManager;
import com.example.chatconcept.user.SessionInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class LoggingSocketHandler extends TextWebSocketHandler {

    private final InMemorySessionRepository sessionRepository;
    private final LoginManager loginManager;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("Got message {} for session {}", message.getPayload(), session.getId());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        UUID userId = loginManager.fromToken((String) session.getAttributes().get(SESSION_TOKEN_KEY))
                .map(SessionInfo::getUserId)
                .orElseThrow(UnknownTokenException::new);
        sessionRepository.insert(userId, session);
    }
}