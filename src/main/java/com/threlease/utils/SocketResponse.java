package com.threlease.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@Builder(builderClassName = "SocketResponseBuilder")
public class SocketResponse {
    private boolean success;
    private Optional<String> message;
    private Optional<Object> data;

    @Override
    public String toString() {
        return "{\n" +
                "\"success\": " + success + ",\n" +
                "\"message\":" + message.map(Object::toString).orElse(null) + ",\n" +
                "\"data\":" + data.map(Object::toString).orElse(null) + "\n"+
                '}';
    }
}
