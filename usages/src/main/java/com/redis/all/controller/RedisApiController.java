package com.redis.all.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RedisApiController {

    RedisTemplate<String, Object> redisTemplate;
    private static final String REDIS = "redis";
    private static final String REDIS_SAMPLE_KEY = "redis_key";

    public RedisApiController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/sample/api/{val}")
    public Map<String, Object> getSomeString(@PathVariable String val) {
        return Map.of("myVal", val);
    }

    // Put a key-value pair <String, Object> to redis
    @PostMapping("/putredis/{val}")
    public Map<String, Object> putIntoRedis(@PathVariable String val) {
        var mapCreated = createRedisValueAsMapObject(val);
        redisTemplate.opsForValue().set(REDIS_SAMPLE_KEY, mapCreated);
        return Map.of(REDIS_SAMPLE_KEY, mapCreated);
    }

    // Get a key-value pair <String, Object> from redis
    @GetMapping("/getredis")
    public Map<String, Object> getFromRedis() {
        Object res = redisTemplate.opsForValue().get(REDIS_SAMPLE_KEY);
        return Map.of(REDIS_SAMPLE_KEY, res);
    }

    private static Map<String, Object> createRedisValueAsMapObject(String val) {
        return Map.of(REDIS, val);
    }

}
