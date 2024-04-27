package com.redis.all.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RedisApiController {

    @GetMapping("/sample/api/{val}")
    public Map<String, Object> getSomeString(@PathVariable String val) {
        return Map.of("myVal", val);
    }

}
