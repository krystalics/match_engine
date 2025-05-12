package com.github.krystalics.service.hello;

import com.github.krystalics.dao.HelloMapper;
import com.github.krystalics.domain.Hello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author linjiabao001
 * @Date 2025/5/12
 * @Description
 */
@Service
public class HelloService {
    @Autowired
    public HelloMapper helloMapper;

    public Hello findByName(String name) {
        return helloMapper.findByName(name);
    }
}
