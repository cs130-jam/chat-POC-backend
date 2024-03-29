package com.example.chatconcept.chat;

import static generated.jooq.tables.ChatroomMembers.CHATROOM_MEMBERS;
import static generated.jooq.tables.Chatrooms.CHATROOMS;

import com.google.common.collect.ImmutableList;
import generated.jooq.tables.records.ChatroomMembersRecord;
import generated.jooq.tables.records.ChatroomsRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class DbChatroomRepository implements ChatroomRepository {

    private final DSLContext context;

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
        return groupOnId(context.select(CHATROOM_MEMBERS.USER, CHATROOMS.asterisk())
                .from(CHATROOM_MEMBERS.leftJoin(CHATROOMS)
                        .on(CHATROOM_MEMBERS.ROOM.eq(CHATROOMS.ID)))
                .where(CHATROOM_MEMBERS.USER.in(context.select(CHATROOM_MEMBERS.USER)
                        .from(CHATROOM_MEMBERS)
                        .where(CHATROOM_MEMBERS.USER.eq(userId))))
                .fetch(PartialChatroom::fromRecord));
    }

    @Override
    public Collection<Chatroom> allChatrooms() {
        return groupOnId(context.select(CHATROOM_MEMBERS.USER, CHATROOMS.asterisk())
                .from(CHATROOM_MEMBERS.leftJoin(CHATROOMS)
                        .on(CHATROOM_MEMBERS.ROOM.eq(CHATROOMS.ID)))
                .fetch(PartialChatroom::fromRecord));

    }

    private Collection<Chatroom> groupOnId(List<PartialChatroom> partials) {
        return partials.stream()
                .collect(Collectors.groupingBy(
                        partial -> partial.getChatroomsRecord().get(CHATROOMS.ID),
                        ChatroomBuilder.collector()))
                .values();
    }

    @Override
    public void insert(Chatroom chatroom) {
        context.insertInto(CHATROOMS)
                .set(toRecord(chatroom))
                .onDuplicateKeyUpdate()
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
    public void insertMember(UUID roomId, UUID userId) {
        context.insertInto(CHATROOM_MEMBERS)
                .set(toMemberRecord(roomId, userId))
                .onDuplicateKeyIgnore()
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

    @Data
    @AllArgsConstructor
    private static class ChatroomBuilder {
        List<UUID> members;
        ChatroomsRecord chatroomsRecord;

        public Chatroom build() {
            return new Chatroom(
                    chatroomsRecord.getId(),
                    ImmutableList.copyOf(members),
                    chatroomsRecord.getUpdated(),
                    chatroomsRecord.getIsdirectmessage());
        }

        public void append(PartialChatroom partialChatroom) {
            members.add(partialChatroom.getUserId());
            chatroomsRecord = partialChatroom.getChatroomsRecord();
        }

        public ChatroomBuilder combine(ChatroomBuilder other) {
            members.addAll(other.members);
            chatroomsRecord = chatroomsRecord == null ? other.chatroomsRecord : chatroomsRecord;
            return this;
        }

        public static Collector<PartialChatroom, ChatroomBuilder, Chatroom> collector() {
            return Collector.of(
                    () -> new ChatroomBuilder(new ArrayList<>(), null),
                    ChatroomBuilder::append,
                    ChatroomBuilder::combine,
                    ChatroomBuilder::build);
        }
    }
}
