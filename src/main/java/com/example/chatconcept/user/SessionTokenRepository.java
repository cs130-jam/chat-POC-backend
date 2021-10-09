package com.example.chatconcept.user;

import java.util.Optional;
import java.util.UUID;

public interface SessionTokenRepository {
    SessionToken getToken(UUID userId);
    Optional<UUID> getUser(SessionToken token);
    void logoutUser(UUID userId);
}
