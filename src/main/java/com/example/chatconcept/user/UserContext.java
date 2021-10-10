package com.example.chatconcept.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.time.Clock;

@Import({
        DbUserRepository.class,
})
public class UserContext {
    @Bean
    public LoginManager loginManager(
            UserRepository userRepository,
            Clock clock,
            ObjectMapper objectMapper,
            @Value("${jwt.secret}") String jwtSecret
    ) {
        return new LoginManager(userRepository, clock, objectMapper, Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)));
    }
}
