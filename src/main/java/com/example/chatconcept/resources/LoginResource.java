package com.example.chatconcept.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.example.chatconcept.LoginManager;
import com.example.chatconcept.SessionToken;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginResource {

    private final LoginManager loginManager;

    @RequestMapping(value = "/login", method = POST,
            consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public SessionToken login(@RequestParam("payload") LoginPayload loginPayload) {
        return loginManager.loginUser(loginPayload.username);
    }

    @Value
    private static class LoginPayload {
        String username;
    }
}
