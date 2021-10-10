package com.example.chatconcept.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

import com.example.chatconcept.user.LoginManager;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginResource {

    private final LoginManager loginManager;

    @PostMapping(value = "/login", consumes = APPLICATION_JSON_VALUE, produces = TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String login(@RequestBody LoginPayload loginPayload) {
        return loginManager.loginUser(loginPayload.username);
    }

    @Value
    private static class LoginPayload {
        String username;
    }
}
