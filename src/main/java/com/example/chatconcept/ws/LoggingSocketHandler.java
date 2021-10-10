package com.example.chatconcept.ws;

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
        UUID userId = loginManager.fromToken(message.getPayload())
                .map(SessionInfo::getUserId)
                .orElseThrow(UnknownTokenException::new);
        sessionRepository.insert(userId, session);
    }
}