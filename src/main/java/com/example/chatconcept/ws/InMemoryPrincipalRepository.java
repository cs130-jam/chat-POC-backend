package com.example.chatconcept.ws;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Singleton;

@Singleton
public class InMemoryPrincipalRepository implements PrincipalRepository {

    private final BiMap<UUID, Principal> data = Maps.synchronizedBiMap(HashBiMap.create());

    @Override
    public Optional<UUID> getByPrincipal(Principal principal) {
        return Optional.ofNullable(data.inverse().get(principal));
    }

    @Override
    public Optional<Principal> getByUser(UUID userId) {
        return Optional.ofNullable(data.get(userId));
    }

    @Override
    public void insert(UUID userId, Principal principal) {
        data.put(userId, principal);
    }
}
