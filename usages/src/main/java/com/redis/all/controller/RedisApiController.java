package com.redis.all.controller;

import com.redis.all.dbops.model.MyDocument;
import com.redis.all.dbops.service.MyDocumentService;
import com.redis.all.dto.MyDocumentRequest;
import com.redis.all.dto.StreamListenRequest;
import com.redis.all.dto.StreamProduceRequest;
import com.redis.all.pubsub.publisher.RedisMessagePublisher;
import com.redis.all.stream.listener.StreamListenerService;
import com.redis.all.stream.producer.StreamProducerService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RedisApiController {

    RedisTemplate<String, Object> redisTemplate;
    RedisMessagePublisher redisMessagePublisher;
    StreamProducerService streamProducerService;
    StreamListenerService streamListenerService;
    MyDocumentService myDocumentService;
    private static final String REDIS = "redis";
    private static final String REDIS_SAMPLE_KEY = "redis_key";

    public RedisApiController(RedisTemplate<String, Object> redisTemplate,
                              RedisMessagePublisher redisMessagePublisher,
                              StreamProducerService streamProducerService,
                              StreamListenerService streamListenerService,
                              MyDocumentService myDocumentService) {
        this.redisTemplate = redisTemplate;
        this.redisMessagePublisher = redisMessagePublisher;
        this.streamProducerService = streamProducerService;
        this.streamListenerService = streamListenerService;
        this.myDocumentService = myDocumentService;
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

    @GetMapping("/pubsub")
    public Map<Integer, Object> startStreaming() {
        Map<Integer, Object> publishResult = new HashMap<>();
        for( int i = 0; i < 100; i++ ) {
            Long result = redisMessagePublisher.publishMessage(String.valueOf(i));
            publishResult.put(i,result);
        }
        return publishResult;
    }

    @PostMapping("/producestream")
    public Map<String, Object> produceStream(@RequestBody StreamProduceRequest streamProduceRequest) {
        Integer numberOfObjects = streamProduceRequest.getNumberOfObjects();
        return streamProducerService.produceMessagesToStream(numberOfObjects);
    }

    @PostMapping("/listenstream")
    public Map<String, Object> listenStream(@RequestBody StreamListenRequest streamListenRequest) {
        Integer batchSize = streamListenRequest.getBatchSize();
        String consumerGroup = streamListenRequest.getConsumerGroup();
        String consumerName = streamListenRequest.getConsumerName();
        return streamListenerService.listenStreamMessages(consumerGroup, consumerName, batchSize);
    }


    @PostMapping("/mydocument")
    public MyDocument saveDocument(@RequestBody MyDocumentRequest myDocumentRequest) {
        MyDocument myDocument = new MyDocument();
        myDocument.setContent(myDocumentRequest.getContent());
        return myDocumentService.saveDocument(myDocument);
    }

    @GetMapping("/mydocument/{id}")
    public MyDocument getDocument(@PathVariable String id) {
        return myDocumentService.getDocumentById(id);
    }

    @PutMapping("/mydocument/{id}")
    public MyDocument updateDocument(@PathVariable String id, @RequestBody MyDocumentRequest myDocumentRequest) {
        return myDocumentService.updateDocument(myDocumentRequest.getContent(), id);
    }

    @DeleteMapping("/mydocument/{id}")
    public Map<String, Object> deleteDocument(@PathVariable String id) {
        myDocumentService.deleteDocument(id);
        return Map.of("deletedDocId", id);
    }

}
