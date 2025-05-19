package com.github.krystalics.service.mq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author linjiabao001
 * @Date 2025/5/13
 * @Description
 */

@Slf4j
@Service
public class Producer {
    @Value("${spring.kafka.topic}")
    private String TOPIC;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
        log.info("消息发送成功: " + message);
    }
}