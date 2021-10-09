package com.example.chatconcept.chat;

import static generated.jooq.tables.ChatroomMembers.CHATROOM_MEMBERS;
import static generated.jooq.tables.Chatrooms.CHATROOMS;

import com.google.common.collect.ImmutableList;
import generated.jooq.tables.records.ChatroomMembersRecord;
import generated.jooq.tables.records.ChatroomsRecord;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class DbChatroomRepository implements ChatroomRepository {

    private final DSLContext context;

    @Autowired
    private DataSource dataSource;

    @Override
    public Optional<Chatroom> get(UUID roomId) {
        List<UUID> chatroomMembers = context.selectFrom(CHATROOM_MEMBERS)
                .where(CHATROOM_MEMBERS.ROOM.eq(roomId))
                .fetch(ChatroomMembersRecord::getUser);
        return context.selectFrom(CHATROOMS)
                .where(CHATROOMS.ID.eq(roomId))
                .fetchOptional(record -> new ChatroomBuilder(ImmutableList.copyOf(chatroomMembers), record))
                .map(ChatroomBuilder::build);
    }

    @Override
    public Collection<Chatroom> getAll(UUID userId) {
        return context.select(CHATROOM_MEMBERS.USER, CHATROOMS.asterisk())
                .from(CHATROOM_MEMBERS.leftJoin(CHATROOMS)
                        .on(CHATROOM_MEMBERS.ROOM.eq(CHATROOMS.ID)))
                .where(CHATROOM_MEMBERS.USER.in(context.select(CHATROOM_MEMBERS.ROOM)
                        .from(CHATROOM_MEMBERS)
                        .where(CHATROOM_MEMBERS.USER.eq(userId))))
                .fetch(PartialChatroom::fromRecord)
                .stream()
                .collect(Collectors.groupingBy(
                        partial -> partial.getChatroomsRecord().get(CHATROOMS.ID),
                        ChatroomBuilder.collector()))
                .values();
    }

    @Override
    public Collection<Chatroom> allChatrooms() {
        return context.select(CHATROOM_MEMBERS.USER, CHATROOMS.asterisk())
                .from(CHATROOM_MEMBERS.leftJoin(CHATROOMS)
                        .on(CHATROOM_MEMBERS.ROOM.eq(CHATROOMS.ID)))
                .fetch(PartialChatroom::fromRecord)
                .stream()
                .collect(Collectors.groupingBy(
                        partial -> partial.getChatroomsRecord().get(CHATROOMS.ID),
                        ChatroomBuilder.collector()))
                .values();
    }

    @Override
    public void insert(Chatroom chatroom) {
        context.insertInto(CHATROOMS)
                .set(toRecord(chatroom))
                .execute();
    }

    @Override
    public void removeMember(UUID roomId, UUID userId) {
        context.deleteFrom(CHATROOM_MEMBERS)
                .where(CHATROOM_MEMBERS.ROOM.eq(roomId)
                        .and(CHATROOM_MEMBERS.USER.eq(userId)))
                .execute();
    }

    @Override
    public void addMember(UUID roomId, UUID userId) {
        context.insertInto(CHATROOM_MEMBERS)
                .set(toMemberRecord(roomId, userId))
                .execute();
    }

    private ChatroomsRecord toRecord(Chatroom chatroom) {
        return new ChatroomsRecord(chatroom.getId(), chatroom.getUpdated(), chatroom.isDirectMessage());
    }

    private ChatroomMembersRecord toMemberRecord(UUID roomId, UUID userId) {
        return new ChatroomMembersRecord(roomId, userId);
    }

    @Value
    public static class PartialChatroom {
        UUID userId;
        ChatroomsRecord chatroomsRecord;

        public static PartialChatroom fromRecord(org.jooq.Record record) {
            return new PartialChatroom(
                    record.get(CHATROOM_MEMBERS.USER),
                    new ChatroomsRecord(
                            record.get(CHATROOMS.ID),
                            record.get(CHATROOMS.UPDATED),
                            record.get(CHATROOMS.ISDIRECTMESSAGE)));
        }
    }

    @Value
    private static class ChatroomBuilder {
        ImmutableList<UUID> members;
        @Nullable
        ChatroomsRecord chatroomsRecord;

        public Chatroom build() {
            return new Chatroom(
                    chatroomsRecord.getId(),
                    members,
                    chatroomsRecord.getUpdated(),
                    chatroomsRecord.getIsdirectmessage());
        }

        public ChatroomBuilder append(PartialChatroom partialChatroom) {
            return new ChatroomBuilder(
                    ImmutableList.<UUID>builder()
                            .addAll(members)
                            .add(partialChatroom.getUserId())
                            .build(),
                    partialChatroom.getChatroomsRecord());
        }

        public ChatroomBuilder combine(ChatroomBuilder other) {
            return new ChatroomBuilder(
                    ImmutableList.<UUID>builder()
                            .addAll(members)
                            .addAll(other.members)
                            .build(),
                    chatroomsRecord);
        }

        public static Collector<PartialChatroom, ChatroomBuilder, Chatroom> collector() {
            return Collector.of(
                    () -> new ChatroomBuilder(ImmutableList.of(), null),
                    ChatroomBuilder::append,
                    ChatroomBuilder::combine,
                    ChatroomBuilder::build);
        }
    }
}
