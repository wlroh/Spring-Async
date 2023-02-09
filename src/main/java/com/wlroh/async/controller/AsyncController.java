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
}
