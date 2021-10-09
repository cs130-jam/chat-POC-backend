package com.example.chatconcept.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.example.chatconcept.user.LoginManager;
import com.example.chatconcept.user.SessionToken;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginResource {

    private final LoginManager loginManager;

    @PostMapping(value = "/login", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public SessionToken login(@RequestBody LoginPayload loginPayload) {
        return loginManager.loginUser(loginPayload.username);
    }

    @Value
    private static class LoginPayload {
        String username;
    }
}
