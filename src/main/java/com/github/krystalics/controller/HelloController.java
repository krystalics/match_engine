package com.github.krystalics.controller;

import com.github.krystalics.domain.Hello;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author linjiabao001
 * @Date 2025/5/11
 * @Description
 */
@Slf4j
@Controller
@RequestMapping("hello/")
public class HelloController {
    @RequestMapping(value = "world", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String hello(@RequestBody Hello customer) {
        log.info("request hello world");
        return "hello world : " + customer.getName();
    }
}
