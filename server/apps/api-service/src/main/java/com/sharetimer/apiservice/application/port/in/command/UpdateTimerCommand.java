package com.sharetimer.apiservice.application.port.in.command;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import com.sharetimer.web.support.exception.BadRequestException;

public record UpdateTimerCommand(String timerId, String ownerToken, Instant requestTime,
    Instant targetTime) {

  public UpdateTimerCommand {
    if (targetTime.isBefore(Instant.now().plus(1, ChronoUnit.MINUTES))) {
      throw new BadRequestException("InvalidTargetTime",
          "'targetTime' must be at least 1 minute after current time.");
    }
  }

}
