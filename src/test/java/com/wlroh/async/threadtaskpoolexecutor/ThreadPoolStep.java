package com.wlroh.async.threadtaskpoolexecutor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.assertj.core.api.Assertions.assertThat;

public class ThreadPoolStep {

    public static void 코어풀_사이즈_조회됨(final ThreadPoolTaskExecutor executor, final int corePoolSize) {
        final int currentCorePoolSize = executor.getCorePoolSize();
        assertThat(currentCorePoolSize).isEqualTo(corePoolSize);
    }

    public static void 최대_풀_사이즈_조회됨(final ThreadPoolTaskExecutor executor, final int maxPoolSize) {
        final int currentMaxPoolSize = executor.getMaxPoolSize();
        assertThat(currentMaxPoolSize).isEqualTo(maxPoolSize);
    }

    public static void 큐_용량_조회됨(final ThreadPoolTaskExecutor executor, final int queueCapacity) {
        final int currentQueueCapacity = executor.getQueueCapacity();
        assertThat(currentQueueCapacity).isEqualTo(queueCapacity);
    }

    public static void 사용중인_풀_사이즈_조회됨(final ThreadPoolTaskExecutor executor, final int poolSize) {
        final int currentPoolSize = executor.getPoolSize();
        assertThat(currentPoolSize).isEqualTo(poolSize);
    }

    public static void 현재_큐_사이즈_조회됨(final ThreadPoolTaskExecutor executor, final int queueSize) {
        final int currentQueueSize = executor.getQueueSize();
        assertThat(currentQueueSize).isEqualTo(queueSize);
    }
}
