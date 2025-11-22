package com.slowhigh.chronos.timer.handler.message;

import java.time.Instant;

public record TimerUpdateTargetTimeMessage(String timerId, String type, Payload payload) {

  public record Payload(Instant updatedAt, Instant serverTime, Instant newTargetTime) {
  }

}
