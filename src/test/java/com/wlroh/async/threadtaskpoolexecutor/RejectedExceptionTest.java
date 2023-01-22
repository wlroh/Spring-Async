package com.wlroh.async.threadtaskpoolexecutor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionException;

import static com.wlroh.async.threadtaskpoolexecutor.ThreadPoolStep.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
public class RejectedExceptionTest {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor3;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor4;

    @Autowired
    private ApplicationContext context;

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

        // when
        threadPoolTaskExecutor4.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor4, 1);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor4, 0);

        threadPoolTaskExecutor4.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor4, 1);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor4, 0);
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
