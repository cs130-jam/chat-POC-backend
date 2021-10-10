package com.example.chatconcept.ws;

import static com.example.chatconcept.user.LoginManager.SESSION_TOKEN_KEY;

import com.example.chatconcept.user.LoginManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class PrincipalHandshakeInterceptor implements HandshakeInterceptor {

    private final LoginManager loginManager;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (!attributes.containsKey(SESSION_TOKEN_KEY)) {
            log.info("ws handshake attributes did not contain session token");
            return false;
        } else {
            return loginManager.fromToken((String) attributes.get(SESSION_TOKEN_KEY)).isPresent();
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception e) { }
}
