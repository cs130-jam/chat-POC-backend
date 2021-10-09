package com.example.chatconcept;

import com.example.chatconcept.chat.ChatContext;
import com.example.chatconcept.resources.ChatResource;
import com.example.chatconcept.resources.ChatroomResource;
import com.example.chatconcept.resources.LoginResource;
import com.example.chatconcept.user.UserContext;
import com.example.chatconcept.util.ObjectMapperProvider;
import com.example.chatconcept.ws.WebSocketContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.time.Clock;

@Import({
        WebSocketContext.class,
        ChatContext.class,
        UserContext.class,

        ChatResource.class,
        ChatroomResource.class,
        LoginResource.class
})
public class ChatConceptContext {

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        return ObjectMapperProvider.get();
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
