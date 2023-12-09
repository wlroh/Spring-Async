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
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(5);
        taskExecutor.setQueueCapacity(3);
        taskExecutor.setThreadNamePrefix("threadPool-");

        taskExecutor.setWaitForTasksToCompleteOnShutdown(true); // 비동기 로직이 모두 수행될 때까지 테스트 코드 종료되지 않기 위해 설정
        taskExecutor.setAwaitTerminationMillis(20_000L);        // 비동기 로직이 모두 수행될 때까지 테스트 코드 종료되지 않기 위해 설정
        return taskExecutor;
    }

    @Bean
    public ThreadPoolTaskExecutor noQueueTaskExecutor() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(3);
        taskExecutor.setQueueCapacity(0);
        taskExecutor.setThreadNamePrefix("noQueue-");

        taskExecutor.setWaitForTasksToCompleteOnShutdown(true); // 비동기 로직이 모두 수행될 때까지 테스트 코드 종료되지 않기 위해 설정
        taskExecutor.setAwaitTerminationMillis(20_000L);        // 비동기 로직이 모두 수행될 때까지 테스트 코드 종료되지 않기 위해 설정
        return taskExecutor;
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor2() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(2);
        taskExecutor.setQueueCapacity(1);
        taskExecutor.setThreadNamePrefix("threadPool2-");

        taskExecutor.setWaitForTasksToCompleteOnShutdown(true); // 비동기 로직이 모두 수행될 때까지 테스트 코드 종료되지 않기 위해 설정
        taskExecutor.setAwaitTerminationMillis(20_000L);        // 비동기 로직이 모두 수행될 때까지 테스트 코드 종료되지 않기 위해 설정
        return taskExecutor;
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor7() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(5);
        taskExecutor.setQueueCapacity(0);
        taskExecutor.setThreadNamePrefix("Executor7-");
        taskExecutor.setKeepAliveSeconds(10);

        taskExecutor.setWaitForTasksToCompleteOnShutdown(true); // 비동기 로직이 모두 수행될 때까지 테스트 코드 종료되지 않기 위해 설정
        taskExecutor.setAwaitTerminationMillis(20_000L);        // 비동기 로직이 모두 수행될 때까지 테스트 코드 종료되지 않기 위해 설정
        return taskExecutor;
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor8() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(5);
        taskExecutor.setQueueCapacity(0);
        taskExecutor.setThreadNamePrefix("Executor8-");
        taskExecutor.setKeepAliveSeconds(10);
        taskExecutor.setAllowCoreThreadTimeOut(true);

        taskExecutor.setWaitForTasksToCompleteOnShutdown(true); // 비동기 로직이 모두 수행될 때까지 테스트 코드 종료되지 않기 위해 설정
        taskExecutor.setAwaitTerminationMillis(20_000L);        // 비동기 로직이 모두 수행될 때까지 테스트 코드 종료되지 않기 위해 설정
        return taskExecutor;
    }

    @Bean
    public ThreadPoolTaskExecutor abortPolicyTaskExecutor() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setQueueCapacity(0);
        taskExecutor.setThreadNamePrefix("abortPolicy-");
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

        taskExecutor.setWaitForTasksToCompleteOnShutdown(true); // 비동기 로직이 모두 수행될 때까지 테스트 코드 종료되지 않기 위해 설정
        taskExecutor.setAwaitTerminationMillis(20_000L);        // 비동기 로직이 모두 수행될 때까지 테스트 코드 종료되지 않기 위해 설정
        return taskExecutor;
    }

    @Bean
    public ThreadPoolTaskExecutor callerRunsPolicyTaskExecutor() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setQueueCapacity(0);
        taskExecutor.setThreadNamePrefix("callerRunsPolicy-");
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        taskExecutor.setWaitForTasksToCompleteOnShutdown(true); // 비동기 로직이 모두 수행될 때까지 테스트 코드 종료되지 않기 위해 설정
        taskExecutor.setAwaitTerminationMillis(20_000L);        // 비동기 로직이 모두 수행될 때까지 테스트 코드 종료되지 않기 위해 설정
        return taskExecutor;
    }

    @Bean
    public ThreadPoolTaskExecutor discardPolicyTaskExecutor() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setQueueCapacity(0);
        taskExecutor.setThreadNamePrefix("discardPolicy-");
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());

        taskExecutor.setWaitForTasksToCompleteOnShutdown(true); // 비동기 로직이 모두 수행될 때까지 테스트 코드 종료되지 않기 위해 설정
        taskExecutor.setAwaitTerminationMillis(20_000L);        // 비동기 로직이 모두 수행될 때까지 테스트 코드 종료되지 않기 위해 설정
        return taskExecutor;
    }

    @Bean
    public ThreadPoolTaskExecutor discardOldestPolicyTaskExecutor() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setQueueCapacity(2);
        taskExecutor.setThreadNamePrefix("discardOldestPolicy-");
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());

        taskExecutor.setWaitForTasksToCompleteOnShutdown(true); // 비동기 로직이 모두 수행될 때까지 테스트 코드 종료되지 않기 위해 설정
        taskExecutor.setAwaitTerminationMillis(20_000L);        // 비동기 로직이 모두 수행될 때까지 테스트 코드 종료되지 않기 위해 설정
        return taskExecutor;
    }
}
