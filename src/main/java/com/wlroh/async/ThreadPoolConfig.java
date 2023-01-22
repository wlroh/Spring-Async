package com.wlroh.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2); // 기본 스레드 수
        taskExecutor.setMaxPoolSize(5); // 최대 스레드 수
        taskExecutor.setThreadNamePrefix("Executor-");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationMillis(20_000L);
        return taskExecutor;
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor2() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2); // 기본 스레드 수
        taskExecutor.setMaxPoolSize(5); // 최대 스레드 수
        taskExecutor.setQueueCapacity(0);
        taskExecutor.setThreadNamePrefix("Executor2-");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationMillis(20_000L);
        return taskExecutor;
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor3() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1); // 기본 스레드 수
        taskExecutor.setMaxPoolSize(1); // 최대 스레드 수
        taskExecutor.setQueueCapacity(0);
        taskExecutor.setThreadNamePrefix("Executor3-");
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationMillis(20_000L);
        return taskExecutor;
    }
}
