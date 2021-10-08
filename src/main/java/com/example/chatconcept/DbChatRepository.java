package com.example.chatconcept;

import static generated.jooq.tables.Chats.CHATS;
import static java.lang.Math.min;

import generated.jooq.tables.records.ChatsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class DbChatRepository implements ChatRepository {

    @Value("${db.chats.max}")
    private final Integer maxChats;
    private final DSLContext context;

    @Override
    public List<Chat> getAfter(UUID room, Instant after) {
        return context.selectFrom(CHATS)
                .where(CHATS.ROOM.eq(room)
                        .and(CHATS.AT.ge(after)))
                .orderBy(CHATS.AT.desc())
                .limit(maxChats)
                .fetch(this::fromRecord);
    }

    @Override
    public List<Chat> getRecent(UUID room, int count) {
        return context.selectFrom(CHATS)
                .where(CHATS.ROOM.eq(room))
                .orderBy(CHATS.AT.desc())
                .limit(min(count, maxChats))
                .fetch(this::fromRecord);
    }

    @Override
    public void save(Chat chat) {
        context.insertInto(CHATS,
                CHATS.ROOM, CHATS.USER, CHATS.MESSAGE, CHATS.AT)
                .values(chat.getRoomId(), chat.getSenderId(), chat.getMessage(), chat.getAt())
                .execute();
    }

    private Chat fromRecord(ChatsRecord record) {
        return new Chat(record.getRoom(), record.getUser(), record.getMessage(), record.getAt());
    }
}
