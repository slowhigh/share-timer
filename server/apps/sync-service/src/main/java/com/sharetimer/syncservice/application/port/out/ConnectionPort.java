package com.sharetimer.syncservice.application.port.out;

import java.time.Instant;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ConnectionPort {
  SseEmitter add(String timerId, SseEmitter emitter);

  void sendTimerEndEvent(String timerId);

  void updateTargetTime(String timerId, Instant updatedAt, Instant serverTime,
      Instant newTargetTime);

  void addTimestamp(String timerId, Instant targetTime, Instant capturedAt);
}
