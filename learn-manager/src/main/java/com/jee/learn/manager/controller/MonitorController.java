package com.jee.learn.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/monitor")
public class MonitorController extends BaseController{

    @GetMapping("hello")
    public String hello() {
        return "hello";
    }
    
}
