package com.sharetimer.syncservice.application.port.out;

import java.time.Instant;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface TimerEventPublisher {
  Flux<ServerSentEvent<Object>> subscribe(String timerId);

  void sendTimerEndEvent(String timerId);

  void updateTargetTime(String timerId, Instant updatedAt, Instant serverTime,
      Instant newTargetTime);

  void addTimestamp(String timerId, Instant targetTime, Instant capturedAt);
}
