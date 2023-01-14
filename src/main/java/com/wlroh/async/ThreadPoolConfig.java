package com.wlroh.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2); // 기본 스레드 수
        taskExecutor.setMaxPoolSize(5); // 최대 스레드 수
        taskExecutor.setQueueCapacity(Integer.MAX_VALUE);
        taskExecutor.setThreadNamePrefix("Executor-");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationMillis(20_000L);
        return taskExecutor;
    }
}