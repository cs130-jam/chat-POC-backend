package com.example.chatconcept.user;

import com.example.chatconcept.UnknownTokenException;
import com.example.chatconcept.UserId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Key;
import java.time.Clock;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class LoginManager implements HandlerMethodArgumentResolver {

    public static final String SESSION_TOKEN_KEY = "session-token";
    private final static Duration TOKEN_TTL = Duration.ofHours(10);

    private final UserRepository userRepository;
    private final Clock clock;
    private final ObjectMapper objectMapper;
    private final Key jwtKey;

    public String loginUser(String username) {
        UUID userId = UUID.randomUUID();
        userRepository.insert(new User(userId, new User.Profile(username)));
        return toToken(new SessionInfo(userId));
    }

    public Optional<SessionInfo> fromToken(String token) {
        try {
            return Optional.of(objectMapper.readValue(
                    Jwts.parserBuilder()
                            .setSigningKey(jwtKey)
                            .setClock(() -> Date.from(clock.instant()))
                            .build()
                            .parseClaimsJws(token)
                            .getBody()
                            .getSubject(),
                    SessionInfo.class));
        } catch (JwtException | JsonProcessingException e) {
            return Optional.empty();
        }
    }

    @SneakyThrows
    private String toToken(SessionInfo sessionInfo) {
        return Jwts.builder()
                .setSubject(objectMapper.writeValueAsString(sessionInfo))
                .setExpiration(Date.from(clock.instant().plus(TOKEN_TTL)))
                .signWith(jwtKey)
                .compact();
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String token = webRequest.getHeader(SESSION_TOKEN_KEY);
        if (token == null || token.length() == 0) {
            throw new UnknownTokenException();
        }

        return fromToken(token)
                .map(SessionInfo::getUserId)
                .orElseThrow(UnknownTokenException::new);
    }
}
