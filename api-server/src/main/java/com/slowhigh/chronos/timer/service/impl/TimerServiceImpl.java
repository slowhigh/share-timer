package com.slowhigh.chronos.timer.service.impl;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.slowhigh.chronos.common.exception.BadRequestException;
import com.slowhigh.chronos.common.exception.ForbiddenException;
import com.slowhigh.chronos.common.exception.NotFoundException;
import com.slowhigh.chronos.timer.domain.Timer;
import com.slowhigh.chronos.timer.domain.Timestamp;
import com.slowhigh.chronos.timer.dto.TimerCreateRes;
import com.slowhigh.chronos.timer.dto.TimerInfoRes;
import com.slowhigh.chronos.timer.dto.TimestampInfoRes;
import com.slowhigh.chronos.timer.handler.TimerHandler;
import com.slowhigh.chronos.timer.repository.TimerRepository;
import com.slowhigh.chronos.timer.service.TimerService;
import com.slowhigh.chronos.timer.service.command.AddTimestampCommand;
import com.slowhigh.chronos.timer.service.command.CreateTimerCommand;
import com.slowhigh.chronos.timer.service.command.DeleteTimerCommand;
import com.slowhigh.chronos.timer.service.command.GetTimerCommand;
import com.slowhigh.chronos.timer.service.command.UpdateTimerCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimerServiceImpl implements TimerService {

  private final TimerRepository timerRepository;
  private final TimerHandler timerHandler;

  @Override
  @Transactional
  public TimerCreateRes createTimer(CreateTimerCommand command) {
    Timer newTimer =
        Timer.builder().targetTime(command.targetTime()).ownerToken(UUID.randomUUID()).build();

    Timer savedTimer = timerRepository.save(newTimer);
    log.debug("신규 타이머 생성: timerId={}, ownerToken={}", savedTimer.getId(),
        savedTimer.getOwnerToken());

    timerHandler.scheduleExpiration(savedTimer.getId().toString(), savedTimer.getTargetTime());

    return new TimerCreateRes(savedTimer.getId().toString(), savedTimer.getOwnerToken().toString());
  }

  @Override
  @Transactional(readOnly = true)
  public TimerInfoRes getTimerInfo(GetTimerCommand command) {
    Timer timer = timerRepository.findById(UUID.fromString(command.timerId()))
        .orElseThrow(() -> new NotFoundException("TimerNotFound",
            "타이머를 찾을 수 없습니다. timerId=" + command.timerId()));

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
  public void deleteTimer(DeleteTimerCommand command) {
    timerRepository.deleteById(UUID.fromString(command.timerId()));
  }

  @Override
  @Transactional
  public void updateTimer(UpdateTimerCommand command) {
    Timer timer = timerRepository.findById(UUID.fromString(command.timerId()))
        .orElseThrow(() -> new NotFoundException("TimerNotFound",
            "타이머를 찾을 수 없습니다. timerId=" + command.timerId()));

    if (command.ownerToken() == null
        || !command.ownerToken().equals(timer.getOwnerToken().toString())) {
      throw new ForbiddenException("OwnerTokenMismatch", "타이머 업데이트 권한이 없습니다.");
    }

    if (command.requestTime().isBefore(timer.getUpdatedAt())) {
      log.warn("오래된 타이머 업데이트 요청 입니다. timerId={}, requestTime={}, updatedAt={}", command.timerId(),
          command.requestTime(), timer.getUpdatedAt());
      return;
    }

    timer.updateTargetTime(command.targetTime());
    Timer savedTimer = timerRepository.saveAndFlush(timer);

    timerHandler.scheduleExpiration(savedTimer.getId().toString(), savedTimer.getTargetTime());
    timerHandler.publishUpdateTimerTargetTime(savedTimer.getId().toString(),
        savedTimer.getUpdatedAt(), savedTimer.getTargetTime());
  }

  @Override
  @Transactional
  public void addTimestamp(AddTimestampCommand command) {
    Timer timer = timerRepository.findById(UUID.fromString(command.timerId()))
        .orElseThrow(() -> new NotFoundException("TimerNotFound",
            "타이머를 찾을 수 없습니다. timerId=" + command.timerId()));

    if (command.capturedAt().isAfter(timer.getTargetTime())) {
      throw new BadRequestException("InvalidCapturedAt",
          "타임스탬프 시각이 타이머 기준 시각보다 이전입니다. capturedAt=" + command.capturedAt());
    }

    Timestamp newTimestamp = Timestamp.builder().timer(timer).targetTime(timer.getTargetTime())
        .capturedAt(command.capturedAt()).build();
    timer.getTimestamps().add(newTimestamp);

    timerHandler.publishAddTimestamp(timer.getId().toString(), timer.getTargetTime(),
        command.capturedAt());
  }

}
