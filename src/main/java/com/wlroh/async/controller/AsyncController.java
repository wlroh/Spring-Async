package com.wlroh.async.controller;

import com.wlroh.async.application.AsyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AsyncController {

    private final AsyncService asyncService;

    @GetMapping("/async-default")
    public String asyncDefault() {
        for (int i = 0; i < 5; i++) {
            asyncService.asyncDefault();
        }
        return "ok";
    }

    @GetMapping("/async-default2")
    public String asyncDefault2() {
        for (int i = 0; i < 5; i++) {
            asyncService.asyncDefault2();
        }
        return "ok";
    }

    @GetMapping("/async-thread-pool")
    public String asyncWithThreadPoolTaskExecutor() {
        for (int i = 0; i < 5; i++) {
            asyncService.asyncWithThreadPoolTaskExecutor();
        }
        return "ok";
    }

    @GetMapping("/async-no-bean-executor")
    public String asyncNoBeanExecutor() {
        for (int i = 0; i < 5; i++) {
            asyncService.asyncNoBeanExecutor();
        }
        return "ok";
    }
}
