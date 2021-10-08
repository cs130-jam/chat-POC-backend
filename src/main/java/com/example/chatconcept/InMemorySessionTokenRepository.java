package com.example.chatconcept;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Singleton;

@RequiredArgsConstructor
@Singleton
public class InMemorySessionTokenRepository implements SessionTokenRepository {

    private final static Duration TOKEN_TTL = Duration.ofHours(10);
    private final BiMap<UUID, SessionToken> data = Maps.synchronizedBiMap(HashBiMap.create());
    private final Map<SessionToken, Instant> expirations = Collections.synchronizedMap(new HashMap<>());
    private final Clock clock;

    @Override
    public SessionToken getToken(UUID userId) {
        SessionToken token = data.computeIfAbsent(userId, ignored -> SessionToken.randomToken());
        expirations.put(token, clock.instant().plus(TOKEN_TTL));
        return token;
    }

    @Override
    public Optional<UUID> getUser(SessionToken token) {
        return Optional.ofNullable(expirations.get(token))
                .flatMap(expiration -> clock.instant().isAfter(expiration)
                        ? Optional.empty()
                        : Optional.ofNullable(data.inverse().get(token)));
    }
}
