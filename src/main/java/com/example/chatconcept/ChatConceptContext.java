package com.example.chatconcept;

import com.example.chatconcept.chat.ChatContext;
import com.example.chatconcept.resources.ChatResource;
import com.example.chatconcept.resources.ChatroomResource;
import com.example.chatconcept.resources.LoginResource;
import com.example.chatconcept.user.LoginManager;
import com.example.chatconcept.user.UserContext;
import com.example.chatconcept.util.ObjectMapperProvider;
import com.example.chatconcept.ws.WebSocketContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Clock;
import java.util.List;

@Import({
        WebSocketContext.class,
        ChatContext.class,
        UserContext.class,

        ChatResource.class,
        ChatroomResource.class,
        LoginResource.class
})
public class ChatConceptContext {

    @Bean
    public WebMvcConfigurer corsConfigurer(LoginManager loginManager) {
        return new WebMvcConfigurer() {
            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                resolvers.add(loginManager);
            }

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000");
            }
        };
    }

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
