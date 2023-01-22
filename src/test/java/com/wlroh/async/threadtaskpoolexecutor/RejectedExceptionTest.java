package com.wlroh.async.threadtaskpoolexecutor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionException;

import static com.wlroh.async.threadtaskpoolexecutor.ThreadPoolStep.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
public class RejectedExceptionTest {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor3;

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
