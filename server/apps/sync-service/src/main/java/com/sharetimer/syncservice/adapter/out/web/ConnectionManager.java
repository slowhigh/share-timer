package com.sharetimer.syncservice.adapter.out.web;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import com.sharetimer.syncservice.adapter.out.web.dto.TargetTimeUpdateEvent;
import com.sharetimer.syncservice.adapter.out.web.dto.TimestampAddedEvent;
import com.sharetimer.syncservice.application.port.out.TimerEventPublisher;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

@Component
@Slf4j
public class ConnectionManager implements TimerEventPublisher {
  private final Map<String, Sinks.Many<ServerSentEvent<Object>>> sinks = new ConcurrentHashMap<>();

  @Override
  public Flux<ServerSentEvent<Object>> subscribe(String timerId) {
    Sinks.Many<ServerSentEvent<Object>> sink = sinks.computeIfAbsent(timerId, k -> {
      log.debug("Sink created: {}", k);
      return Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
    });

    return sink.asFlux().mergeWith(Flux.interval(Duration.ZERO, Duration.ofSeconds(30))
        .map(i -> ServerSentEvent.builder().comment("heartbeat").build())).doOnCancel(() -> {
          log.debug("Subscription cancelled: {}", timerId);
        }).doFinally(signalType -> {
          log.debug("Subscriber count: {}", sink.currentSubscriberCount());
        });
  }

  @Override
  public void sendTimerEndEvent(String timerId) {
    Sinks.Many<ServerSentEvent<Object>> sink = sinks.get(timerId);
    if (sink == null) {
      log.warn("No target for TimerEnd event, timerId: {}", timerId);
      return;
    }

    sink.tryEmitNext(ServerSentEvent.builder().event("timerEnd").data(Map.of()).build());
    sink.tryEmitComplete();
    sinks.remove(timerId);
    log.debug("TimerEnd event sent and Sink removed, timerId: {}", timerId);
  }

  @Override
  public void updateTargetTime(String timerId, Instant updatedAt, Instant serverTime,
      Instant newTargetTime) {
    emitEvent(timerId, "targetTimeUpdate",
        new TargetTimeUpdateEvent(updatedAt, serverTime, newTargetTime));
  }

  @Override
  public void addTimestamp(String timerId, Instant targetTime, Instant capturedAt) {
    emitEvent(timerId, "timestampAdd", new TimestampAddedEvent(targetTime, capturedAt));
  }

  private void emitEvent(String timerId, String eventName, Object data) {
    Sinks.Many<ServerSentEvent<Object>> sink = sinks.get(timerId);
    if (sink == null) {
      log.warn("No target for {} event, timerId: {}", eventName, timerId);
      return;
    }

    Sinks.EmitResult result =
        sink.tryEmitNext(ServerSentEvent.builder().event(eventName).data(data).build());
    if (result.isFailure()) {
      log.warn("Failed to emit {} event for timerId {}: {}", eventName, timerId, result);
    } else {
      log.debug("{} event sent successfully, timerId: {}", eventName, timerId);
    }
  }
}
