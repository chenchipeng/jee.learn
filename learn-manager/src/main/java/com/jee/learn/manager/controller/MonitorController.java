package com.jee.learn.manager.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${system.authc-path}/monitor")
public class MonitorController extends BaseController {

    @Async
    @GetMapping("hello")
    public CompletableFuture<String> hello() {
        return CompletableFuture.completedFuture("hello");
    }

}
