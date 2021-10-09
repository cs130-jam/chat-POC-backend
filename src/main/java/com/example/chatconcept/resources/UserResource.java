package com.example.chatconcept.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.example.chatconcept.UnknownUserException;
import com.example.chatconcept.user.User;
import com.example.chatconcept.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserResource {

    private final UserRepository userRepository;

    @GetMapping(value = "user", produces = APPLICATION_JSON_VALUE)
    public User getSessionUser(@UserId UUID userId) {
        return userRepository.find(userId)
                .orElseThrow(() -> new UnknownUserException("User unknown for given session id"));
    }

    @GetMapping(value = "user/{userId}", produces = APPLICATION_JSON_VALUE)
    public User getUser(@PathVariable UUID userId) {
        return userRepository.find(userId)
                .orElseThrow(() -> new UnknownUserException("User unknown for given session id"));
    }
}
