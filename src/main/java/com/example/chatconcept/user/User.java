package com.example.chatconcept.user;

import com.example.chatconcept.util.JsonConverter;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class User {
    @NonNull UUID id;
    Profile profile;

    @Value
    public static class Profile {
        String username;

        public static class Converter extends JsonConverter<Profile> {}
    }
}
