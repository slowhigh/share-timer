package com.sharetimer.syncservice.application.service;

import java.io.IOException;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.sharetimer.core.common.config.RedisTemplateFactory;
import com.sharetimer.core.common.config.TimerProps;
import com.sharetimer.syncservice.adapter.out.external.TimerApiClient;
import com.sharetimer.syncservice.application.port.in.TimerUseCase;
import com.sharetimer.syncservice.application.port.out.ConnectionPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimerService implements TimerUseCase {

  private final ConnectionPort connectionPort;
  private final RedisTemplateFactory redisTemplateFactory;
  private final TimerProps timerProps;
  private final TimerApiClient timerApiClient;

  @Override
  public SseEmitter subscribe(String timerId) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

    String key = timerProps.getExpiration().getKeyPrefix() + timerId;
    if (!redisTemplateFactory.getTemplate(timerProps.getExpiration().getDbIndex()).hasKey(key)) {
      try {
        emitter.send(SseEmitter.event().name("timerNotFound"));
        emitter.complete();
      } catch (IOException e) {
        log.error("SSE 에러 전송 중 클라이언트 연결 종료, timerId: {}", timerId, e);
        emitter.completeWithError(e);
      }
      return emitter;
    }

    connectionPort.add(timerId, emitter);
    return emitter;
  }

  @Override
  public void processTimerExpiration(String timerId) {
    connectionPort.sendTimerEndEvent(timerId);
    timerApiClient.deleteTimer(timerId);
  }

  @Override
  public void updateTargetTime(String timerId, Instant updatedAt, Instant serverTime,
      Instant newTargetTime) {
    connectionPort.updateTargetTime(timerId, updatedAt, serverTime, newTargetTime);
  }

  @Override
  public void addTimestamp(String timerId, Instant targetTime, Instant capturedAt) {
    connectionPort.addTimestamp(timerId, targetTime, capturedAt);
  }
}
