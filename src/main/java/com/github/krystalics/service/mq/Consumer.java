package com.github.krystalics.service.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @Author linjiabao001
 * @Date 2025/5/13
 * @Description
 */
@Slf4j
@Service
public class Consumer {

    @KafkaListener(topics = "${spring.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String message) {
        log.info("接收到消息: " + message);
    }

}