package com.github.krystalics.service.mq;

import com.alibaba.fastjson.JSON;
import com.github.krystalics.service.match_engine.QLExpressMatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        Map data = JSON.parseObject(message, Map.class);
//        QLExpressMatcher.processBatch(Arrays.asList(data),"");
    }

}