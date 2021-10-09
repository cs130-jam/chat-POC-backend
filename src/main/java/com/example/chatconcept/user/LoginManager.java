package com.example.chatconcept.user;

import com.example.chatconcept.UnknownTokenException;
import com.example.chatconcept.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class LoginManager implements HandlerMethodArgumentResolver {

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

    public void logoutUser(UUID userId) {
        tokenRepository.logoutUser(userId);
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

        SessionToken sessionToken = SessionToken.fromString(token);
        return userIdForToken(sessionToken)
                .orElseThrow(UnknownTokenException::new);
    }
}
