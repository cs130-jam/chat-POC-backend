package com.example.chatconcept.ws;

import com.example.chatconcept.LoginManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Import({
        InMemoryPrincipalRepository.class,
        WebSocketConfig.class,
        LoginManager.class
})
public class WebSocketContext {

    @Bean
    public WebSocketManager webSocketManager(
            PrincipalRepository principalRepository,
            LoginManager loginManager,
            SimpMessagingTemplate simpMessagingTemplate
    ) {
        return new WebSocketManager(principalRepository, loginManager, simpMessagingTemplate);
    }
}
