package com.wlroh.async.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncService {

    @Async
    public void asyncDefault() {
        // SimpleAsyncTaskExecutor 가 신규 Thread 생성하여 Task 수행
        log.info("call Test Method");
        try {
            Thread.sleep(5_000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("end Test Method");
    }

    @Async
    public void asyncDefault2() {
        // SimpleAsyncTaskExecutor 가 신규 Thread 생성하여 Task 수행
        log.info("call Test Method");
        try {
            Thread.sleep(5_000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("end Test Method");
    }

    @Async("threadPoolTaskExecutor4")
    public void asyncWithThreadPoolTaskExecutor() {
        // threadPoolTaskExecutor4 가 관리하는 Thread 이용해 Task 수행
        log.info("call Test Method");
        try {
            Thread.sleep(5_000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("end Test Method");
    }

    @Async("noBeanExecutor")
    public void asyncNoBeanExecutor() {
        // NoSuchBeanDefinitionException 예외 발생
        log.info("call Test Method");
        try {
            Thread.sleep(5_000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("end Test Method");
    }
}
