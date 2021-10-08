package com.example.chatconcept.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({
        DbUserRepository.class,
        InMemorySessionTokenRepository.class
})
public class UserContext {
    @Bean
    public LoginManager loginManager(
            UserRepository userRepository,
            InMemorySessionTokenRepository tokenRepository
    ) {
        return new LoginManager(userRepository, tokenRepository);
    }
}
