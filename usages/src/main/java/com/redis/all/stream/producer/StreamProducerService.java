package com.redis.all.stream.producer;

import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static com.redis.all.constant.Constant.REDIS_STREAM_KEY;

@Service
public class StreamProducerService {

    private RedisTemplate<String, Object> redisTemplate;

    public StreamProducerService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Map<String, Object> produceMessagesToStream(Integer numberOfObjects) {
        Map<String, Object> producedStreamRecords = new LinkedHashMap<>();
        for(int i=0; i<numberOfObjects; i++) {
            HashMap<String, Object> streamMap = new HashMap<>();
            streamMap.put(UUID.randomUUID().toString(), String.valueOf(i));
            ObjectRecord<String, HashMap<String, Object>> record = StreamRecords.objectBacked(streamMap).withStreamKey(REDIS_STREAM_KEY);
            RecordId recordId = redisTemplate.opsForStream().add(record);
            System.out.println("Message sent to stream. RecordId: " + recordId.getValue());
            producedStreamRecords.put(recordId.getValue(), record);
        }
        return producedStreamRecords;
    }
}
