package com.wlroh.async.threadtaskpoolexecutor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class ThreadTaskPoolSizeTest {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor2;

    @DisplayName("executeTask() 를 비동기로 실행한다.")
    @Test
    void asyncTest() {
        System.out.println("start call thread pool");

        for (int i = 0; i < 5; i++) {
            threadPoolTaskExecutor.execute(this::executeTaskForFiveSeconds);
            final int poolSize = threadPoolTaskExecutor.getPoolSize();
            System.out.println("poolSize = " + poolSize);
        }

        System.out.println("end");
    }

    @DisplayName("corePool 이 모두 사용중이면 Queue 에 등록이 된다.")
    @Test
    void queueTest() {
        // given
        코어풀_사이즈_조회됨(threadPoolTaskExecutor, 2);

        // when
        threadPoolTaskExecutor.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor, 1);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor, 0);

        threadPoolTaskExecutor.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor, 2);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor, 0);

        threadPoolTaskExecutor.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor, 2);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor, 1);

        threadPoolTaskExecutor.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor, 2);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor, 2);

        threadPoolTaskExecutor.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor, 2);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor, 3);
    }

    @DisplayName("corePool 이 모두 사용중이고 QueueCapacity 가 가득차면 신규 Pool 이 생성된다.")
    @Test
    void queueCapacity_noValue() {
        // given
        코어풀_사이즈_조회됨(threadPoolTaskExecutor2, 2);
        큐_용량_조회됨(threadPoolTaskExecutor2, 0);

        // when
        threadPoolTaskExecutor2.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor2, 1);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor2, 0);

        threadPoolTaskExecutor2.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor2, 2);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor2, 0);

        threadPoolTaskExecutor2.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor2, 3);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor2, 0);

        threadPoolTaskExecutor2.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor2, 4);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor2, 0);

        threadPoolTaskExecutor2.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor2, 5);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor2, 0);
    }

    private void 코어풀_사이즈_조회됨(final ThreadPoolTaskExecutor executor, final int corePoolSize) {
        final int currentCorePoolSize = executor.getCorePoolSize();
        assertThat(currentCorePoolSize).isEqualTo(corePoolSize);
    }

    private void 최대_풀_사이즈_조회됨(final ThreadPoolTaskExecutor executor, final int maxPoolSize) {
        final int currentMaxPoolSize = executor.getMaxPoolSize();
        assertThat(currentMaxPoolSize).isEqualTo(maxPoolSize);
    }

    private void 큐_용량_조회됨(final ThreadPoolTaskExecutor executor, final int queueCapacity) {
        final int currentQueueCapacity = executor.getQueueCapacity();
        assertThat(currentQueueCapacity).isEqualTo(queueCapacity);
    }

    private void 사용중인_풀_사이즈_조회됨(final ThreadPoolTaskExecutor executor, final int poolSize) {
        final int currentPoolSize = executor.getPoolSize();
        assertThat(currentPoolSize).isEqualTo(poolSize);
    }

    private void 현재_큐_사이즈_조회됨(final ThreadPoolTaskExecutor executor, final int queueSize) {
        final int currentQueueSize = executor.getQueueSize();
        assertThat(currentQueueSize).isEqualTo(queueSize);
    }

    private void executeTaskForFiveSeconds() {
        log.info("do task");
        try {
            Thread.sleep(5_000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("end do task");
    }
}
