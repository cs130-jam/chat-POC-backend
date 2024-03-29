package com.example.chatconcept.user;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> find(UUID userId);
    void insert(User user);
}
