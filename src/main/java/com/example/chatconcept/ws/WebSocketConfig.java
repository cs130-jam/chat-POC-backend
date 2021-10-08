package com.example.chatconcept.ws;

import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final PrincipalHandshakeInterceptor handshakeInterceptor;
    private final LoggingSocketHandler socketHandler;

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler, "/ws")
                .setAllowedOrigins("http://localhost:3000")
                .addInterceptors(handshakeInterceptor)
                .withSockJS();
    }
}
