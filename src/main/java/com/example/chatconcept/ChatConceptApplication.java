package com.example.chatconcept;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
        ChatConceptContext.class
})

@Configuration
@EnableAutoConfiguration
public class ChatConceptApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatConceptApplication.class, args);
    }

}
