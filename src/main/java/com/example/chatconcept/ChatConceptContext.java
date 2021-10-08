package com.example.chatconcept;

import com.example.chatconcept.util.ObjectMapperProvider;
import com.example.chatconcept.ws.WebSocketContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.time.Clock;

@Import({
        WebSocketContext.class,

        LoginManager.class,
        ChatManager.class,

        InMemorySessionTokenRepository.class,
        DbChatRepository.class,
        DbChatroomRepository.class,
        DbUserRepository.class
})
public class ChatConceptContext {

    @Bean
    public DSLContext dslContext(HikariDataSource dataSource) {
        return DSL.using(dataSource, SQLDialect.MYSQL);
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
