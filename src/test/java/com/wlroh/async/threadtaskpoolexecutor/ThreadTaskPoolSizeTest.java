package com.wlroh.async.threadtaskpoolexecutor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.LocalDateTime;
import java.util.concurrent.RejectedExecutionException;

import static com.wlroh.async.threadtaskpoolexecutor.ThreadPoolStep.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Slf4j
public class ThreadTaskPoolSizeTest {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private ThreadPoolTaskExecutor noQueueTaskExecutor;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor2;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor7;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor8;

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
        큐_용량_조회됨(threadPoolTaskExecutor, 3);

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
    void new_thread_test() {
        // given
        코어풀_사이즈_조회됨(threadPoolTaskExecutor, 2);
        최대_풀_사이즈_조회됨(threadPoolTaskExecutor, 5);
        큐_용량_조회됨(threadPoolTaskExecutor, 3);

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

        threadPoolTaskExecutor.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor, 3);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor, 3);

        threadPoolTaskExecutor.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor, 4);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor, 3);

        threadPoolTaskExecutor.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor, 5);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor, 3);
    }

    @DisplayName("maxPoolSize 에 도달하면 기본 정책일 경우 예외를 반환한다.")
    @Test
    void overflowMaxPoolSize() {
        // given
        코어풀_사이즈_조회됨(noQueueTaskExecutor, 1);
        최대_풀_사이즈_조회됨(noQueueTaskExecutor, 3);
        큐_용량_조회됨(noQueueTaskExecutor, 0);

        // when
        noQueueTaskExecutor.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(noQueueTaskExecutor, 1);

        noQueueTaskExecutor.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(noQueueTaskExecutor, 2);

        noQueueTaskExecutor.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(noQueueTaskExecutor, 3);

        assertThatThrownBy(() -> noQueueTaskExecutor.execute(this::executeTaskForFiveSeconds))
                .isInstanceOf(RejectedExecutionException.class);
    }

    @DisplayName("큐가 가득찬 후 요청 건들은 신규 쓰레드에서 바로 수행된다.")
    @Test
    void test() {
        // given
        코어풀_사이즈_조회됨(threadPoolTaskExecutor2, 1);
        최대_풀_사이즈_조회됨(threadPoolTaskExecutor2, 2);
        큐_용량_조회됨(threadPoolTaskExecutor2, 1);

        // when
        threadPoolTaskExecutor2.execute(() -> executeTaskForFiveSeconds(1));
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor2, 1);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor2, 0);

        threadPoolTaskExecutor2.execute(() -> executeTaskForFiveSeconds(2));
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor2, 1);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor2, 1);

        threadPoolTaskExecutor2.execute(() -> executeTaskForFiveSeconds(3));
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor2, 2);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor2, 1);
    }

    @DisplayName("CorePoolSize 를 초과한 Pool 은 사용 후 설정된 keepAliveSeconds(기본값 60초) 시간 이후 종료된다.")
    @Test
    void poolShutdownTest() throws InterruptedException {
        // given
        코어풀_사이즈_조회됨(threadPoolTaskExecutor2, 2);
        최대_풀_사이즈_조회됨(threadPoolTaskExecutor2, 5);
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

        final LocalDateTime start = LocalDateTime.now().plusSeconds(65);
        LocalDateTime now = LocalDateTime.now();
        while (start.isAfter(now)) {
            now = LocalDateTime.now();
            Thread.sleep(1_000L);
        }

        // then
        assertThat(threadPoolTaskExecutor2.getPoolSize()).isEqualTo(threadPoolTaskExecutor2.getCorePoolSize());
        assertThat(threadPoolTaskExecutor2.getActiveCount()).isZero();
    }

    @DisplayName("setKeepAliveSeconds 설정 시 사용이 완료된 Pool 종료 시점을 조절할 수 있다.")
    @Test
    void keepAliveSecondsTest() throws InterruptedException {
        // given
        코어풀_사이즈_조회됨(threadPoolTaskExecutor7, 1);
        최대_풀_사이즈_조회됨(threadPoolTaskExecutor7, 5);
        큐_용량_조회됨(threadPoolTaskExecutor7, 0);

        // when
        threadPoolTaskExecutor7.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor7, 1);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor7, 0);

        threadPoolTaskExecutor7.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor7, 2);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor7, 0);

        final LocalDateTime start = LocalDateTime.now().plusSeconds(15);
        LocalDateTime now = LocalDateTime.now();
        while (start.isAfter(now)) {
            now = LocalDateTime.now();
            Thread.sleep(1_000L);
        }

        // then
        assertThat(threadPoolTaskExecutor7.getPoolSize()).isEqualTo(threadPoolTaskExecutor7.getCorePoolSize());
        assertThat(threadPoolTaskExecutor7.getActiveCount()).isZero();
    }

    @DisplayName("setAllowCoreThreadTimeOut 설정으로 CoreThreadPool 도 종료할 수 있다.")
    @Test
    void setAllowCoreThreadTimeOutTest() throws InterruptedException {
        // given
        코어풀_사이즈_조회됨(threadPoolTaskExecutor8, 1);
        최대_풀_사이즈_조회됨(threadPoolTaskExecutor8, 5);
        큐_용량_조회됨(threadPoolTaskExecutor8, 0);

        // when
        threadPoolTaskExecutor8.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor8, 1);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor8, 0);

        threadPoolTaskExecutor8.execute(this::executeTaskForFiveSeconds);
        사용중인_풀_사이즈_조회됨(threadPoolTaskExecutor8, 2);
        현재_큐_사이즈_조회됨(threadPoolTaskExecutor8, 0);

        final LocalDateTime start = LocalDateTime.now().plusSeconds(15);
        LocalDateTime now = LocalDateTime.now();
        while (start.isAfter(now)) {
            now = LocalDateTime.now();
            Thread.sleep(1_000L);
        }

        // then
        assertThat(threadPoolTaskExecutor8.getPoolSize()).isZero();
        assertThat(threadPoolTaskExecutor8.getActiveCount()).isZero();
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

    private void executeTaskForFiveSeconds(final int sequence) {
        log.info("do task[{}]", sequence);
        try {
            Thread.sleep(5_000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("end do task[{}]", sequence);
    }
}
