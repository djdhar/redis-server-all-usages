package com.redis.all.dto;

import lombok.Data;

@Data
public class StreamListenRequest {
    Integer batchSize;
    String consumerGroup;
    String consumerName;
}
