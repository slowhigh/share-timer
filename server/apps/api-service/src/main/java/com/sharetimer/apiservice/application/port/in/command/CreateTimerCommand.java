package com.sharetimer.apiservice.application.port.in.command;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import com.sharetimer.web.support.exception.BadRequestException;

public record CreateTimerCommand(Instant targetTime) {

  public CreateTimerCommand {
    if (targetTime.isBefore(Instant.now().plus(1, ChronoUnit.MINUTES))) {
      throw new BadRequestException("InvalidTargetTime",
          "'targetTime' must be at least 1 minute after current time.");
    }
  }

}
