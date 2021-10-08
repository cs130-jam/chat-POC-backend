package com.example.chatconcept.chat;

import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

import java.time.Instant;
import java.util.UUID;

@Value
public class Chatroom {
    @NonNull UUID id;
    ImmutableList<UUID> members;
    @With
    Instant updated;
    boolean isDirectMessage;
}
