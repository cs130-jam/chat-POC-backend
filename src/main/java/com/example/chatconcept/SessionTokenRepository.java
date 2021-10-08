package com.example.chatconcept;

import java.util.Optional;
import java.util.UUID;

public interface SessionTokenRepository {
    SessionToken getToken(UUID userId);
    Optional<UUID> getUser(SessionToken token);
}
