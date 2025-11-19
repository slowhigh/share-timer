package com.slowhigh.kairos.timer.service.impl;

import java.io.IOException;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.slowhigh.kairos.common.config.StringRedisTemplateFactory;
import com.slowhigh.kairos.timer.client.TimerApiClient;
import com.slowhigh.kairos.timer.config.TimerProps;
import com.slowhigh.kairos.timer.handler.SseEmitters;
import com.slowhigh.kairos.timer.service.TimerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimerServiceImpl implements TimerService {

  private final SseEmitters sseEmitters;
  private final StringRedisTemplateFactory stringRedisTemplateFactory;
  private final TimerProps timerProps;
  private final TimerApiClient timerApiClient;

  @Override
  public SseEmitter subscribe(String timerId) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

    String key = timerProps.getExpiration().getKeyPrefix() + timerId;
    if (!stringRedisTemplateFactory.getTemplate(timerProps.getExpiration().getDbIndex())
        .hasKey(key)) {
      try {
        emitter.send(SseEmitter.event().name("timerNotFound"));
        emitter.complete();
      } catch (IOException e) {
        log.error("SSE 에러 전송 중 클라이언트 연결 종료, timerId: {}", timerId, e);
        emitter.completeWithError(e);
      }
      return emitter;
    }

    sseEmitters.add(timerId, emitter);
    return emitter;
  }

  @Override
  public void processTimerExpiration(String timerId) {
    sseEmitters.sendTimerEndEvent(timerId);
    timerApiClient.deleteTimer(timerId);
  }

  @Override
  public void updateTargetTime(String timerId, Instant updatedAt, Instant serverTime, Instant newTargetTime) {
    sseEmitters.updateTargetTime(timerId, updatedAt, serverTime, newTargetTime);
  }

  @Override
  public void addTimestamp(String timerId, Instant targetTime, Instant capturedAt) {
    sseEmitters.addTimestamp(timerId, targetTime, capturedAt);
  }
}
