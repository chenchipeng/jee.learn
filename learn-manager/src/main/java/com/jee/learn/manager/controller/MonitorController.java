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
    @GetMapping("")
    public CompletableFuture<String> hello() {
        return CompletableFuture.completedFuture("main/monitor");
    }

    @Async
    @GetMapping("404")
    public CompletableFuture<String> notFound() {
        return CompletableFuture.completedFuture("error/404");
    }

    @Async
    @GetMapping("500")
    public CompletableFuture<String> internalServerError() {
        return CompletableFuture.completedFuture("error/500");
    }

    @Async
    @GetMapping("error")
    public CompletableFuture<String> error() {
        throw new NullPointerException("作死的空指针");
    }

}
