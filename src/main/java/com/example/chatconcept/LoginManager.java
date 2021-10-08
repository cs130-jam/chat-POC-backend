package com.example.chatconcept;

import com.example.chatconcept.resources.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class LoginManager implements WebArgumentResolver {

    public static final String SESSION_TOKEN_KEY = "session-token";

    private final UserRepository userRepository;
    private final SessionTokenRepository tokenRepository;

    public Optional<UUID> userIdForToken(SessionToken token) {
        return tokenRepository.getUser(token);
    }

    public SessionToken loginUser(String username) {
        UUID userId = UUID.randomUUID();
        userRepository.insert(new User(userId, new User.Profile(username)));
        return tokenRepository.getToken(userId);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) {
        if (methodParameter.hasMethodAnnotation(UserId.class)) {
            String tokenString = webRequest.getHeader(SESSION_TOKEN_KEY);
            if (tokenString == null || tokenString.length() == 0) {
                throw new UnknownTokenException();
            }

            SessionToken token = SessionToken.fromString(tokenString);
            return userIdForToken(token)
                    .orElseThrow(UnknownTokenException::new);
        }
        return UNRESOLVED;
    }
}
