package com.sharetimer.syncservice.application.port.in;

import java.time.Instant;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface TimerUseCase {

  SseEmitter subscribe(String timerId);

  void processTimerExpiration(String timerId);

  void updateTargetTime(String timerId, Instant updatedAt, Instant serverTime,
      Instant newTargetTime);

  void addTimestamp(String timerId, Instant targetTime, Instant capturedAt);

}
