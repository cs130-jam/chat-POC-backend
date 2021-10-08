package com.example.chatconcept.user;

import lombok.Value;

import java.util.UUID;

@Value
public class SessionToken {
    UUID token;

    public String toString() {
        return token.toString();
    }

    public static SessionToken fromString(String token) {
        return new SessionToken(UUID.fromString(token));
    }

    public static SessionToken randomToken() {
        return new SessionToken(UUID.randomUUID());
    }
}
