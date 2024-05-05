package com.redis.all.pubsub.subscriber;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class RedisMessageSubscriber implements MessageListener {

    private ObjectMapper objectMapper;

    public RedisMessageSubscriber(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        Map<String, JsonNode> messageBody = null;
        try {
            messageBody = decode(message.getBody());
            // Process the received message
            System.out.println("SUCCESS ::: Received message: " + messageBody + " from channel: " + channel);
        } catch (IOException e) {
            System.out.println("FAILURE ::: Unable to receive any message from channel: " + channel);
        }
    }

    Map<String, JsonNode> decode(final byte[] byteArrayResult) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(byteArrayResult);
        return objectMapper.convertValue(jsonNode, Map.class);
    }
}
