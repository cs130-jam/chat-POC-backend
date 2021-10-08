package com.example.chatconcept.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Converter;

@Slf4j
public class JsonConverter<T> implements Converter<String, T> {

    private final Class<T> toType;
    private final ObjectReader reader;
    private final ObjectWriter writer;

    @SuppressWarnings("unchecked")
    public JsonConverter() {
        TypeReference genericType = new TypeReference<T>() {};
        ObjectMapper mapper = ObjectMapperProvider.get();
        reader = mapper.readerFor(genericType);
        writer = mapper.writerFor(genericType);
        toType = (Class<T>) mapper.constructType(genericType).getRawClass();
    }

    @Override
    public T from(String s) {
        try {
            return s == null ? null : reader.readValue(s);
        } catch (JsonProcessingException e) {
            log.error("Failed to read json {} to object: {}", s, e);
            return null;
        }
    }

    @Override
    public String to(T t) {
        try {
            return t == null ? null : writer.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            log.error("Failed to write to {} to json: {}", t, e);
            return null;
        }
    }

    @Override
    public Class<String> fromType() {
        return String.class;
    }

    @Override
    public Class<T> toType() {
        return toType;
    }
}
