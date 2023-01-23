package com.wlroh.async.threadtaskpoolexecutor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionException;

import static com.wlroh.async.threadtaskpoolexecutor.ThreadPoolStep.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
public class RejectedExceptionTest {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor3;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor4;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor5;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor6;

    @DisplayName("AbortPolicy 의 경우 항상 예외를 반환한다.")
    @Test
    void abortPolicyTest() {
        // given
        코어풀_사이즈_조회됨(threadPoolTaskExecutor3, 1);
        최대_풀_사이즈_조회됨(threadPoolTaskExecutor3, 1);
        큐_용량_조회됨(threadPoolTaskExecutor3, 0);

        // when
        threadPoolTaskExecutor3.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor3, 1);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor3, 0);

        assertThatThrownBy(() -> threadPoolTaskExecutor3.execute(this::executeTaskForFiveSeconds))
                .isInstanceOf(RejectedExecutionException.class);
    }

    @DisplayName("CallerRunsPolicy 의 경우 요청 쓰레드가 종료되지 않으면 요청 쓰레드에서 진행한다.")
    @Test
    void callerRunsPolicyTest() {
        // given
        코어풀_사이즈_조회됨(threadPoolTaskExecutor4, 1);
        최대_풀_사이즈_조회됨(threadPoolTaskExecutor4, 1);
        큐_용량_조회됨(threadPoolTaskExecutor4, 0);
        final Thread requestThread = Thread.currentThread();

        // when
        threadPoolTaskExecutor4.execute(() -> this.executeTaskForFiveSeconds(requestThread, false));
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor4, 1);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor4, 0);

        threadPoolTaskExecutor4.execute(() -> this.executeTaskForFiveSeconds(requestThread, true));
    }

    @DisplayName("DiscardPolicy 의 경우 거절된 Task 의 경우 버려진다.")
    @Test
    void discardPolicyTest() {
        // given
        코어풀_사이즈_조회됨(threadPoolTaskExecutor5, 1);
        최대_풀_사이즈_조회됨(threadPoolTaskExecutor5, 1);
        큐_용량_조회됨(threadPoolTaskExecutor5, 0);

        // when
        threadPoolTaskExecutor5.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor5, 1);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor5, 0);

        threadPoolTaskExecutor5.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor5, 1);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor5, 0);

        threadPoolTaskExecutor5.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor5, 1);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor5, 0);
    }

    @DisplayName("DiscardOldestPolicy 의 경우 가장 오래된 큐의 Task 를 버리고 새로운 Task를 ")
    @Test
    void discardOldestPolicyTest() {
        // given
        코어풀_사이즈_조회됨(threadPoolTaskExecutor6, 1);
        최대_풀_사이즈_조회됨(threadPoolTaskExecutor6, 1);
        큐_용량_조회됨(threadPoolTaskExecutor6, 2);

        // when
        threadPoolTaskExecutor6.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor6, 1);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor6, 0);

        threadPoolTaskExecutor6.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor6, 1);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor6, 1);

        threadPoolTaskExecutor6.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor6, 1);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor6, 2);

        threadPoolTaskExecutor6.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor6, 1);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor6, 2);
    }

    @DisplayName("가장 오랜된 Queue 는 등록된 큐중에 제일 처음에 등록된 큐이다.")
    @Test
    void oldestQueueTest() {
        threadPoolTaskExecutor6.execute(() -> this.executeTaskForFiveSeconds(1));
        threadPoolTaskExecutor6.execute(() -> this.executeTaskForFiveSeconds(2));
        threadPoolTaskExecutor6.execute(() -> this.executeTaskForFiveSeconds(3));
        threadPoolTaskExecutor6.execute(() -> this.executeTaskForFiveSeconds(4));
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

    private void executeTaskForFiveSeconds(final Thread requestThread, final boolean isSameThread) {
        final Thread thread = Thread.currentThread();
        if (isSameThread) {
            assertThat(requestThread == thread).isTrue();
        } else {
            assertThat(requestThread == thread).isFalse();
        }
        executeTaskForFiveSeconds();
    }

    private void executeTaskForFiveSeconds(final int id) {
        log.info("id={}", id);
        executeTaskForFiveSeconds();
    }
}
