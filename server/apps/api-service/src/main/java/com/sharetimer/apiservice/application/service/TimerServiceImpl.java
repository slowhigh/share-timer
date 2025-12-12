package com.sharetimer.apiservice.application.service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sharetimer.core.common.exception.BadRequestException;
import com.sharetimer.core.common.exception.ForbiddenException;
import com.sharetimer.core.common.exception.NotFoundException;
import com.sharetimer.apiservice.domain.model.Timer;
import com.sharetimer.apiservice.domain.model.Timestamp;
import com.sharetimer.apiservice.adapter.in.web.dto.TimerCreateRes;
import com.sharetimer.apiservice.adapter.in.web.dto.TimerInfoRes;
import com.sharetimer.apiservice.adapter.in.web.dto.TimestampInfoRes;
import com.sharetimer.apiservice.adapter.out.persistence.TimerRepository;
import com.sharetimer.apiservice.application.port.in.TimerUseCase;
import com.sharetimer.apiservice.application.port.out.TimerEventPort;
import com.sharetimer.apiservice.application.port.in.command.AddTimestampCommand;
import com.sharetimer.apiservice.application.port.in.command.CreateTimerCommand;
import com.sharetimer.apiservice.application.port.in.command.DeleteTimerCommand;
import com.sharetimer.apiservice.application.port.in.command.GetTimerCommand;
import com.sharetimer.apiservice.application.port.in.command.UpdateTimerCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimerServiceImpl implements TimerUseCase {

  private final TimerRepository timerRepository;
  private final TimerEventPort timerEventPort;

  @Override
  @Transactional
  public TimerCreateRes createTimer(CreateTimerCommand command) {
    Timer newTimer =
        Timer.builder().targetTime(command.targetTime()).ownerToken(UUID.randomUUID()).build();

    Timer savedTimer = timerRepository.save(newTimer);
    log.debug("신규 타이머 생성: timerId={}, ownerToken={}", savedTimer.getId(),
        savedTimer.getOwnerToken());

    timerEventPort.scheduleExpiration(savedTimer.getId().toString(), savedTimer.getTargetTime());

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

    timerEventPort.scheduleExpiration(savedTimer.getId().toString(), savedTimer.getTargetTime());
    timerEventPort.publishUpdateTimerTargetTime(savedTimer.getId().toString(),
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

    timerEventPort.publishAddTimestamp(timer.getId().toString(), timer.getTargetTime(),
        command.capturedAt());
  }

}
