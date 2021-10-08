package com.example.chatconcept;

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
