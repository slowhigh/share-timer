package com.slowhigh.chronos.timer.service.command;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import com.slowhigh.chronos.common.exception.BadRequestException;

public record CreateTimerCommand(Instant targetTime) {

  public CreateTimerCommand {
    if (targetTime.isBefore(Instant.now().plus(1, ChronoUnit.MINUTES))) {
      throw new BadRequestException("InvalidTargetTime", "'targetTime'은 현재 시간보다 최소 1분 이후여야 합니다.");
    }
  }

}
