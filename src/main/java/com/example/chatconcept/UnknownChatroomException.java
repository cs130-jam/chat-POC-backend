package com.example.chatconcept;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class UnknownChatroomException extends RuntimeException {
    public UnknownChatroomException(String message) {
        super(message);
    }
}
