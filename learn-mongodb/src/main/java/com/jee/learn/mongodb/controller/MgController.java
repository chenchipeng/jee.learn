package com.jee.learn.mongodb.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jee.learn.mongodb.domain.MgUser;
import com.jee.learn.mongodb.repository.MgUserRepository;

@RestController
public class MgController {

    private Logger logger = LoggerFactory.getLogger(MgController.class);

    @Autowired
    private MgUserRepository mgUserRepository;

    @GetMapping("/")
    public String hello() {
        String str = "hello world";
        logger.debug("controller 收到请求后返回 {}", str);
        return str;
    }

    @GetMapping("all")
    public List<MgUser> all() {
        return mgUserRepository.findAll();
    }

    @GetMapping("name")
    public MgUser name(@RequestParam String name) {
        return mgUserRepository.findByName(name);
    }

}
