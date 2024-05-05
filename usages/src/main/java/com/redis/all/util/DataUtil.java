package com.redis.all.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataUtil {

    public static final String MESSAGE_ID = "message_id";
    public static final String MESSAGE_TEXT = "message_text";
    ObjectMapper objectMapper;
    public DataUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    public JsonNode createMessageNode(String message) {
        return objectMapper.createObjectNode()
                .put(MESSAGE_ID, UUID.randomUUID().toString())
                .put(MESSAGE_TEXT, message);
    }
}
