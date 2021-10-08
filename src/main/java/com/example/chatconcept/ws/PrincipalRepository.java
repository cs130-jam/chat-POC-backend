package com.example.chatconcept.ws;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

public interface PrincipalRepository {
    Optional<UUID> getByPrincipal(Principal principal);
    Optional<Principal> getByUser(UUID userId);
    void insert(UUID userId, Principal principal);
}
