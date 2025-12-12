package com.sharetimer.apiservice.application.port.out;

import java.time.Instant;

public interface TimerEventPort {
  void scheduleExpiration(String timerId, Instant targetTime);

  void publishUpdateTimerTargetTime(String timerId, Instant updatedAt, Instant targetTime);

  void publishAddTimestamp(String timerId, Instant targetTime, Instant capturedAt);
}
