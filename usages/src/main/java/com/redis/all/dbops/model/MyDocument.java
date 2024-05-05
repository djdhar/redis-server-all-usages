package com.redis.all.dbops.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("MyDocument")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyDocument {

    @Id
    private String id;
    private String content;
}