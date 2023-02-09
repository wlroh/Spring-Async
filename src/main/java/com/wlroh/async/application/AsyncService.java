package com.wlroh.async.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncService {

    @Async
    public void asyncDefault() {
        // myThreadPoolTaskExecutor 이름의 ThreadPoolTaskExecutor bean 이 관리하는 Thread 이용해 Task 수행
        log.info("call Test Method");
        try {
            Thread.sleep(5_000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("end Test Method");
    }
}
