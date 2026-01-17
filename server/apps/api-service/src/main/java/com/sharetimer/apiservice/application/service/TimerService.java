package com.sharetimer.apiservice.application.service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sharetimer.apiservice.adapter.in.web.dto.TimerCreateRes;
import com.sharetimer.apiservice.adapter.in.web.dto.TimerInfoRes;
import com.sharetimer.apiservice.adapter.in.web.dto.TimestampInfoRes;
import com.sharetimer.apiservice.application.port.in.TimerUseCase;
import com.sharetimer.apiservice.application.port.in.command.AddTimestampCommand;
import com.sharetimer.apiservice.application.port.in.command.CreateTimerCommand;
import com.sharetimer.apiservice.application.port.in.command.DeleteTimerCommand;
import com.sharetimer.apiservice.application.port.in.command.GetTimerCommand;
import com.sharetimer.apiservice.application.port.in.command.UpdateTimerCommand;
import com.sharetimer.apiservice.application.port.out.LoadTimerPort;
import com.sharetimer.apiservice.application.port.out.SaveTimerPort;
import com.sharetimer.apiservice.application.port.out.TimerEventPort;
import com.sharetimer.apiservice.domain.model.Timer;
import com.sharetimer.apiservice.domain.model.Timestamp;
import com.sharetimer.web.support.exception.BadRequestException;
import com.sharetimer.web.support.exception.ForbiddenException;
import com.sharetimer.web.support.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimerService implements TimerUseCase {

  private final LoadTimerPort loadTimerPort;
  private final SaveTimerPort saveTimerPort;
  private final TimerEventPort timerEventPort;

  @Override
  @Transactional
  public TimerCreateRes createTimer(CreateTimerCommand command) {
    Timer newTimer =
        Timer.builder().targetTime(command.targetTime()).ownerToken(UUID.randomUUID()).build();

    Timer savedTimer = saveTimerPort.saveTimer(newTimer);
    log.debug("New timer created: timerId={}, ownerToken={}", savedTimer.getId(),
        savedTimer.getOwnerToken());

    timerEventPort.scheduleExpiration(savedTimer.getId().toString(), savedTimer.getTargetTime());

    return new TimerCreateRes(savedTimer.getId().toString(), savedTimer.getOwnerToken().toString());
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "timers", key = "#command.timerId", unless = "#result == null")
  public TimerInfoRes getTimerInfo(GetTimerCommand command) {
    Timer timer = loadTimerPort.loadTimer(UUID.fromString(command.timerId()))
        .orElseThrow(() -> new NotFoundException("TimerNotFound",
            "Timer not found. timerId=" + command.timerId()));

    Instant serverTime = Instant.now();
    boolean isOwner = command.ownerToken() != null
        && UUID.fromString(command.ownerToken()).equals(timer.getOwnerToken());

    List<TimestampInfoRes> timestamps = timer.getTimestamps().stream()
        .sorted(Comparator.comparing(Timestamp::getCapturedAt))
        .map(
            timestamp -> new TimestampInfoRes(timestamp.getTargetTime(), timestamp.getCapturedAt()))
        .toList();

    return new TimerInfoRes(timer.getUpdatedAt(), timer.getTargetTime(), serverTime, timestamps,
        isOwner);
  }

  @Override
  @Transactional
  @CacheEvict(value = "timers", key = "#command.timerId")
  public void deleteTimer(DeleteTimerCommand command) {
    saveTimerPort.deleteTimer(UUID.fromString(command.timerId()));
  }

  @Override
  @Transactional
  @CacheEvict(value = "timers", key = "#command.timerId")
  public void updateTimer(UpdateTimerCommand command) {
    Timer timer = loadTimerPort.loadTimer(UUID.fromString(command.timerId()))
        .orElseThrow(() -> new NotFoundException("TimerNotFound",
            "Timer not found. timerId=" + command.timerId()));

    if (command.ownerToken() == null
        || !command.ownerToken().equals(timer.getOwnerToken().toString())) {
      throw new ForbiddenException("OwnerTokenMismatch", "No permission to update timer.");
    }

    if (command.requestTime().isBefore(timer.getUpdatedAt())) {
      log.warn("Old timer update request. timerId={}, requestTime={}, updatedAt={}", command.timerId(),
          command.requestTime(), timer.getUpdatedAt());
      return;
    }

    timer.updateTargetTime(command.targetTime());
    Timer savedTimer = saveTimerPort.saveTimer(timer);

    timerEventPort.scheduleExpiration(savedTimer.getId().toString(), savedTimer.getTargetTime());
    timerEventPort.publishUpdateTimerTargetTime(savedTimer.getId().toString(),
        savedTimer.getUpdatedAt(), savedTimer.getTargetTime());
  }

  @Override
  @Transactional
  @CacheEvict(value = "timers", key = "#command.timerId")
  public void addTimestamp(AddTimestampCommand command) {
    Timer timer = loadTimerPort.loadTimer(UUID.fromString(command.timerId()))
        .orElseThrow(() -> new NotFoundException("TimerNotFound",
            "Timer not found. timerId=" + command.timerId()));

    if (command.capturedAt().isAfter(timer.getTargetTime())) {
      throw new BadRequestException("InvalidCapturedAt",
          "Timestamp time is before timer target time. capturedAt=" + command.capturedAt());
    }

    Timestamp newTimestamp = Timestamp.builder().timer(timer).targetTime(timer.getTargetTime())
        .capturedAt(command.capturedAt()).build();
    timer.getTimestamps().add(newTimestamp);

    timerEventPort.publishAddTimestamp(timer.getId().toString(), timer.getTargetTime(),
        command.capturedAt());
  }

}
