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
    private ThreadPoolTaskExecutor abortPolicyTaskExecutor;

    @Autowired
    private ThreadPoolTaskExecutor callerRunsPolicyTaskExecutor;

    @Autowired
    private ThreadPoolTaskExecutor discardPolicyTaskExecutor;

    @Autowired
    private ThreadPoolTaskExecutor discardOldestPolicyTaskExecutor;

    @DisplayName("AbortPolicy 의 경우 항상 예외를 반환한다.")
    @Test
    void abortPolicyTest() {
        // given
        코어풀_사이즈_조회됨(abortPolicyTaskExecutor, 1);
        최대_풀_사이즈_조회됨(abortPolicyTaskExecutor, 1);
        큐_용량_조회됨(abortPolicyTaskExecutor, 0);

        // when
        abortPolicyTaskExecutor.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(abortPolicyTaskExecutor, 1);
        현재_큐_사이즈_조회됨(abortPolicyTaskExecutor, 0);

        assertThatThrownBy(() -> abortPolicyTaskExecutor.execute(this::executeTaskForFiveSeconds))
                .isInstanceOf(RejectedExecutionException.class);
    }

    @DisplayName("CallerRunsPolicy 의 경우 요청 쓰레드가 종료되지 않으면 요청 쓰레드에서 진행한다.")
    @Test
    void callerRunsPolicyTest() {
        // given
        코어풀_사이즈_조회됨(callerRunsPolicyTaskExecutor, 1);
        최대_풀_사이즈_조회됨(callerRunsPolicyTaskExecutor, 1);
        큐_용량_조회됨(callerRunsPolicyTaskExecutor, 0);
        final Thread requestThread = Thread.currentThread();

        // when
        callerRunsPolicyTaskExecutor.execute(() -> this.executeTaskForFiveSeconds(requestThread, false));
        사용중인_풀_사이즈_조회됨(callerRunsPolicyTaskExecutor, 1);
        현재_큐_사이즈_조회됨(callerRunsPolicyTaskExecutor, 0);

        callerRunsPolicyTaskExecutor.execute(() -> this.executeTaskForFiveSeconds(requestThread, true));
    }

    @DisplayName("DiscardPolicy 의 경우 거절된 Task 의 경우 버려진다.")
    @Test
    void discardPolicyTest() {
        // given
        코어풀_사이즈_조회됨(discardPolicyTaskExecutor, 1);
        최대_풀_사이즈_조회됨(discardPolicyTaskExecutor, 1);
        큐_용량_조회됨(discardPolicyTaskExecutor, 0);

        // when
        discardPolicyTaskExecutor.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(discardPolicyTaskExecutor, 1);
        현재_큐_사이즈_조회됨(discardPolicyTaskExecutor, 0);

        discardPolicyTaskExecutor.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(discardPolicyTaskExecutor, 1);
        현재_큐_사이즈_조회됨(discardPolicyTaskExecutor, 0);

        discardPolicyTaskExecutor.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(discardPolicyTaskExecutor, 1);
        현재_큐_사이즈_조회됨(discardPolicyTaskExecutor, 0);
    }

    @DisplayName("DiscardOldestPolicy 의 경우 가장 오래된 큐의 Task 를 버리고 새로운 Task를 큐에 등록한다")
    @Test
    void discardOldestPolicyTest() {
        // given
        코어풀_사이즈_조회됨(discardOldestPolicyTaskExecutor, 1);
        최대_풀_사이즈_조회됨(discardOldestPolicyTaskExecutor, 1);
        큐_용량_조회됨(discardOldestPolicyTaskExecutor, 2);

        // when
        discardOldestPolicyTaskExecutor.execute(() -> this.executeTaskForFiveSeconds(1));
        사용중인_풀_사이즈_조회됨(discardOldestPolicyTaskExecutor, 1);
        현재_큐_사이즈_조회됨(discardOldestPolicyTaskExecutor, 0);

        discardOldestPolicyTaskExecutor.execute(() -> this.executeTaskForFiveSeconds(2));
        사용중인_풀_사이즈_조회됨(discardOldestPolicyTaskExecutor, 1);
        현재_큐_사이즈_조회됨(discardOldestPolicyTaskExecutor, 1);

        discardOldestPolicyTaskExecutor.execute(() -> this.executeTaskForFiveSeconds(3));
        사용중인_풀_사이즈_조회됨(discardOldestPolicyTaskExecutor, 1);
        현재_큐_사이즈_조회됨(discardOldestPolicyTaskExecutor, 2);

        discardOldestPolicyTaskExecutor.execute(() -> this.executeTaskForFiveSeconds(4));
        사용중인_풀_사이즈_조회됨(discardOldestPolicyTaskExecutor, 1);
        현재_큐_사이즈_조회됨(discardOldestPolicyTaskExecutor, 2);
    }

    @DisplayName("가장 오랜된 Queue 는 등록된 큐중에 제일 처음에 등록된 큐이다.")
    @Test
    void oldestQueueTest() {
        discardOldestPolicyTaskExecutor.execute(() -> this.executeTaskForFiveSeconds(1));
        discardOldestPolicyTaskExecutor.execute(() -> this.executeTaskForFiveSeconds(2));
        discardOldestPolicyTaskExecutor.execute(() -> this.executeTaskForFiveSeconds(3));
        discardOldestPolicyTaskExecutor.execute(() -> this.executeTaskForFiveSeconds(4));
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
        log.info("do task[{}]", id);
        try {
            Thread.sleep(5_000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("end do task[{}]", id);
    }
}
