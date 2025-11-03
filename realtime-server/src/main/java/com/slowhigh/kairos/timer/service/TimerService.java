package com.slowhigh.kairos.timer.service;

import java.time.Instant;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface TimerService {

  SseEmitter subscribe(String timerId);

  void processTimerExpiration(String timerId);

  void updateTargetTime(String timerId, Instant updatedAt, Instant serverTime, Instant newTargetTime); 

  void addTimestamp(String timerId, Instant targetTime, Instant capturedAt);

}
