package com.example.chatconcept.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.example.chatconcept.UserId;
import com.example.chatconcept.user.LoginManager;
import com.example.chatconcept.user.SessionToken;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LoginResource {

    private final LoginManager loginManager;

    @PostMapping(value = "/login", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SessionToken login(@RequestBody LoginPayload loginPayload) {
        return loginManager.loginUser(loginPayload.username);
    }

    @PostMapping(value = "/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@UserId UUID userId) {
        loginManager.logoutUser(userId);
    }

    @Value
    private static class LoginPayload {
        String username;
    }
}
