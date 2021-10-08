package com.example.chatconcept.chat;

import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class Chat {
    @NonNull UUID roomId;
    @NonNull UUID senderId;
    String message;
    Instant at;
}
