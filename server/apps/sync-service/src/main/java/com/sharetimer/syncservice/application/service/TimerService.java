package com.sharetimer.syncservice.application.service;

import java.time.Instant;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import com.sharetimer.storage.redis.config.ReactiveRedisTemplateFactory;
import com.sharetimer.storage.redis.config.TimerRedisProps;
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
  private final TimerRedisProps timerRedisProps;
  private final TimerApiClient timerApiClient;

  @Override
  public Flux<ServerSentEvent<Object>> subscribe(String timerId) {
    String key = timerRedisProps.getExpiration().getKeyPrefix() + timerId;
    return redisTemplateFactory.getTemplate(timerRedisProps.getExpiration().getDbIndex())
        .hasKey(key).flatMapMany(hasKey -> {
          if (!Boolean.TRUE.equals(hasKey)) {
            return Flux.just(ServerSentEvent.builder().event("timerNotFound").build());
          }
          return timerEventPublisher.subscribe(timerId);
        });
  }

  @Override
  public void processTimerExpiration(String timerId) {
    timerEventPublisher.sendTimerEndEvent(timerId);
    timerApiClient.deleteTimer(timerId).subscribe(
        success -> log.debug("Timer delete request success: {}", timerId),
        error -> log.error("Timer delete request failed: {}", timerId, error));
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
