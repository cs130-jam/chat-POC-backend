package com.example.chatconcept.ws;

import com.example.chatconcept.user.LoginManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({
        WebSocketConfig.class,
        LoggingSocketHandler.class,
        PrincipalHandshakeInterceptor.class,

        InMemorySessionRepository.class,

        LoginManager.class
})
public class WebSocketContext {

    @Bean
    public WebSocketManager webSocketManager(InMemorySessionRepository sessionRepository) {
        return new WebSocketManager(sessionRepository);
    }
}
