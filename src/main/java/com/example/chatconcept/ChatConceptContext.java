package com.example.chatconcept;

import com.example.chatconcept.chat.ChatManager;
import com.example.chatconcept.chat.DbChatRepository;
import com.example.chatconcept.chat.DbChatroomRepository;
import com.example.chatconcept.resources.ChatResource;
import com.example.chatconcept.resources.ChatroomResource;
import com.example.chatconcept.resources.LoginResource;
import com.example.chatconcept.user.DbUserRepository;
import com.example.chatconcept.user.InMemorySessionTokenRepository;
import com.example.chatconcept.user.LoginManager;
import com.example.chatconcept.util.ObjectMapperProvider;
import com.example.chatconcept.ws.WebSocketContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
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
        DbUserRepository.class,

        ChatResource.class,
        ChatroomResource.class,
        LoginResource.class
})
public class ChatConceptContext {

    @Bean
    public DSLContext dslContext(
            @Value("${db.datasource.username}") String dbUsername,
            @Value("${db.datasource.password}") String dbPassword,
            @Value("${db.datasource.url}") String jdbcUrl
    ) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        config.setMaximumPoolSize(10);
        config.setMaxLifetime(1800000);

        return DSL.using(new HikariDataSource(config), SQLDialect.MYSQL);
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
