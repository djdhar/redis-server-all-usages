package com.redis.all.pubsub.publisher;

import com.redis.all.util.DataUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.redis.all.constant.Constant.PUB_SUB_KEY;
import static com.redis.all.constant.Constant.REDIS_CHANNEL;

@Service
public class RedisMessagePublisher {

    private RedisTemplate<String, Object> redisTemplate;
    private DataUtil dataUtil;

    public  RedisMessagePublisher(RedisTemplate<String, Object> redisTemplate, DataUtil dataUtil) {
        this.redisTemplate = redisTemplate;
        this.dataUtil = dataUtil;
    }


    public Long publishMessage(String message) {
        return redisTemplate.convertAndSend(REDIS_CHANNEL,
                Map.of(PUB_SUB_KEY, dataUtil.createMessageNode(message)));
    }
}
