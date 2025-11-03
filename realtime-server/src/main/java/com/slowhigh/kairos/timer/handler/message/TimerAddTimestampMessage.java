package com.slowhigh.kairos.timer.handler.message;

import java.time.Instant;

public record TimerAddTimestampMessage(String timerId, String type, Payload payload) {

  public record Payload(Instant targetTime, Instant capturedAt) {
  }

}
