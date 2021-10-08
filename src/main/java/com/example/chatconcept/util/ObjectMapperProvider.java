package com.example.chatconcept.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjectMapperProvider {

    public static ObjectMapper get() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }
}
