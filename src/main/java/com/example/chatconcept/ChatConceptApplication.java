package com.example.chatconcept;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({
        ChatConceptContext.class
})
@SpringBootApplication
public class ChatConceptApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatConceptApplication.class, args);
    }

}
