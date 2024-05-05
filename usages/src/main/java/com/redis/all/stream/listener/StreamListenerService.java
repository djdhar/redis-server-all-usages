package com.redis.all.stream.listener;

import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.redis.all.constant.Constant.REDIS_STREAM_KEY;

@Service
public class StreamListenerService {

    private RedisTemplate<String, Object> redisTemplate;
    private static final String CONSUMER_GROUP = "consumer_group";
    private static final String CONSUMER_NAME = "consumer_name";
    private static final String RESPONSE_SIZE = "response_size";

    public StreamListenerService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public Map<String, Object> listenStreamMessages(String consumerGroup, String consumerName, Integer batchSize) {

        Map<String, Object> listenStreamResult = new LinkedHashMap<>();
        listenStreamResult.put(CONSUMER_GROUP, consumerGroup);
        listenStreamResult.put(CONSUMER_NAME, consumerName);

        StreamOperations<String, Object, Object> streamOps = redisTemplate.opsForStream();

        try {
            streamOps.createGroup(REDIS_STREAM_KEY, ReadOffset.from("0"), consumerGroup);
        } catch (Exception e) {
            // Group might already exist, ignore exception
            System.out.println(e.getStackTrace()[0]);
        }

        List<MapRecord<String, Object, Object>> messages = redisTemplate.opsForStream().read(Consumer.from(consumerGroup, consumerName),
                StreamReadOptions.empty().count(batchSize),
                StreamOffset.create(REDIS_STREAM_KEY, ReadOffset.lastConsumed()));

        if (Objects.nonNull(messages)) {
            System.out.println("Size of the message batch :: " + messages.size());
            listenStreamResult.put(RESPONSE_SIZE, messages.size());
        }

        for (MapRecord<String, Object, Object> record : messages) {
            System.out.println("Received message: " + record.getValue() +
                    ", offset id : " + record.getId());
            listenStreamResult.put(record.getId().toString(), record);
            // Acknowledge the message to mark it as processed
            streamOps.acknowledge(consumerGroup, record);
        }
        return listenStreamResult;
    }
}