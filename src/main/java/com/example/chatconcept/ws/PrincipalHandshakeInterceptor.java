package com.example.chatconcept.ws;

import static com.example.chatconcept.user.LoginManager.SESSION_TOKEN_KEY;

import com.example.chatconcept.user.LoginManager;
import com.example.chatconcept.user.SessionToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class PrincipalHandshakeInterceptor implements HandshakeInterceptor {

    private final LoginManager loginManager;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (!attributes.containsKey(SESSION_TOKEN_KEY)) {
            log.info("ws handshake attributes did not contain session token");
            return false;
        }

        Optional<UUID> userId = loginManager.userIdForToken(SessionToken.fromString((String) attributes.get(SESSION_TOKEN_KEY)));
        if (userId.isPresent()) {
            return true;
        } else {
            log.info("No user found for given session token");
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception e) { }
}
