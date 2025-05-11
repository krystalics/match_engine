package com.github.krystalics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author linjiabao001
 * @Date 2025/5/11
 * @Description
 */

@SpringBootApplication
@RestController
public class MatchEngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatchEngineApplication.class, args);
    }
}
