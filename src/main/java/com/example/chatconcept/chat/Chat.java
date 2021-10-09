package com.example.chatconcept.chat;

import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;
import javax.annotation.Nullable;

@Value
public class Chat {
    @Nullable Integer id;
    @NonNull UUID roomId;
    @NonNull UUID senderId;
    String message;
    Instant at;
}
