package com.sharetimer.syncservice.application.service;

import java.time.Instant;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import com.sharetimer.core.common.config.ReactiveRedisTemplateFactory;
import com.sharetimer.core.common.config.TimerProps;
import com.sharetimer.syncservice.adapter.out.external.TimerApiClient;
import com.sharetimer.syncservice.application.port.in.TimerUseCase;
import com.sharetimer.syncservice.application.port.out.TimerEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimerService implements TimerUseCase {

  private final TimerEventPublisher timerEventPublisher;
  private final ReactiveRedisTemplateFactory redisTemplateFactory;
  private final TimerProps timerProps;
  private final TimerApiClient timerApiClient;

  @Override
  public Flux<ServerSentEvent<Object>> subscribe(String timerId) {
    String key = timerProps.getExpiration().getKeyPrefix() + timerId;
    return redisTemplateFactory.getTemplate(timerProps.getExpiration().getDbIndex()).hasKey(key)
        .flatMapMany(hasKey -> {
          if (!Boolean.TRUE.equals(hasKey)) {
            return Flux.just(ServerSentEvent.builder().event("timerNotFound").build());
          }
          return timerEventPublisher.subscribe(timerId);
        });
  }

  @Override
  public void processTimerExpiration(String timerId) {
    timerEventPublisher.sendTimerEndEvent(timerId);
    timerApiClient.deleteTimer(timerId).subscribe(success -> log.debug("타이머 삭제 요청 성공: {}", timerId),
        error -> log.error("타이머 삭제 요청 실패: {}", timerId, error));
  }

  @Override
  public void updateTargetTime(String timerId, Instant updatedAt, Instant serverTime,
      Instant newTargetTime) {
    timerEventPublisher.updateTargetTime(timerId, updatedAt, serverTime, newTargetTime);
  }

  @Override
  public void addTimestamp(String timerId, Instant targetTime, Instant capturedAt) {
    timerEventPublisher.addTimestamp(timerId, targetTime, capturedAt);
  }
}
