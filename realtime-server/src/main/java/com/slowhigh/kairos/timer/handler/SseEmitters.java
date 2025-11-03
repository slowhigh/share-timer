package com.slowhigh.kairos.timer.handler;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.slowhigh.kairos.timer.dto.TargetTimeUpdateEvent;
import com.slowhigh.kairos.timer.dto.TimestampAddedEvent;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SseEmitters {
  private final Map<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();
  private final ScheduledExecutorService heartbeatScheduler = Executors.newScheduledThreadPool(1);
  private final Map<SseEmitter, ScheduledFuture<?>> heartbeats = new ConcurrentHashMap<>();

  public SseEmitter add(String timerId, SseEmitter emitter) {
    List<SseEmitter> emitterList =
        this.emitters.computeIfAbsent(timerId, k -> new CopyOnWriteArrayList<>());
    emitterList.add(emitter);

    log.debug("추가 성공, timerId: {}, 현재 구독자 수: {}", timerId, emitterList.size());

    setupHeartbeat(emitter, timerId);
    setupCallback(emitter, timerId);

    return emitter;
  }

  public void sendTimerEndEvent(String timerId) {
    List<SseEmitter> emitterList = emitters.get(timerId);
    if (emitterList == null || emitterList.isEmpty()) {
      log.warn("TimerEnd 이벤트 전송 대상 없음, timerId: {}", timerId);
      return;
    }

    int successCount = 0;

    for (SseEmitter emitter : emitterList) {
      try {
        emitter.send(SseEmitter.event().name("timerEnd").data(Map.of()));
        successCount++;
      } catch (IOException e) {
        log.error("TimerEnd 이벤트 전송 실패, timerId: {}, 예외: {}", timerId, e.getMessage());
      } finally {
        emitter.complete();
      }
    }

    log.debug("TimerEnd 이벤트 전송 완료, timerId: {}, 구독자 수: {}, 성공 수: {}", timerId, emitterList.size(),
        successCount);
  }

  public void updateTargetTime(String timerId, Instant updatedAt, Instant serverTime,
      Instant newTargetTime) {
    List<SseEmitter> emitterList = emitters.get(timerId);
    if (emitterList == null || emitterList.isEmpty()) {
      log.warn("TargetTimeUpdate 이벤트 전송 대상 없음, timerId: {}", timerId);
      return;
    }

    for (SseEmitter emitter : emitterList) {
      try {
        emitter.send(SseEmitter.event().name("targetTimeUpdate")
            .data(new TargetTimeUpdateEvent(updatedAt, serverTime, newTargetTime)));
        log.debug("TargetTimeUpdate 이벤트 전송 성공, timerId: {}, newTargetTime: {}", timerId,
            newTargetTime);
      } catch (IOException e) {
        log.error("TargetTimeUpdate 이벤트 전송 실패, timerId: {}, 예외: {}", timerId, e.getMessage());
      }
    }
  }

  public void addTimestamp(String timerId, Instant targetTime, Instant capturedAt) {
    List<SseEmitter> emitterList = emitters.get(timerId);
    if (emitterList == null || emitterList.isEmpty()) {
      log.warn("TimestampAdd 이벤트 전송 대상 없음, timerId: {}", timerId);
      return;
    }

    for (SseEmitter emitter : emitterList) {
      try {
        emitter.send(SseEmitter.event().name("timestampAdd")
            .data(new TimestampAddedEvent(targetTime, capturedAt)));
        log.debug("TimestampAdd 이벤트 전송 성공, timerId: {}, targetTime: {}, capturedAt: {}", timerId,
            targetTime, capturedAt);
      } catch (IOException e) {
        log.error("TimestampAdd 이벤트 전송 실패, timerId: {}, 예외: {}", timerId, e.getMessage());
      }
    }
  }

  private void setupHeartbeat(SseEmitter emitter, String timerId) {
    ScheduledFuture<?> heartbeatTask = heartbeatScheduler.scheduleAtFixedRate(() -> {
      try {
        emitter.send(SseEmitter.event().comment("heartbeat"));
        log.debug("Heartbeat 전송 성공, timerId: {}", timerId);
      } catch (IOException e) {
        log.warn("Heartbeat 전송 실패, timerId: {}", timerId);
      }
    }, 0, 30, TimeUnit.SECONDS);

    heartbeats.put(emitter, heartbeatTask);
  }

  private void setupCallback(SseEmitter emitter, String timerId) {
    emitter.onCompletion(() -> {
      log.debug("완료 콜백 호출, timerId: {}", timerId);

      ScheduledFuture<?> task = heartbeats.remove(emitter);
      if (task != null) {
        task.cancel(true);
      }

      this.emitters.computeIfPresent(timerId, (id, list) -> {
        list.remove(emitter);
        log.debug("제거 성공, timerId: {}, 현재 구독자 수: {}", id, list.size());
        if (list.isEmpty()) {
          log.debug("Map에서 timerId 제거: {}", id);
          return null;
        }
        return list;
      });
    });

    emitter.onTimeout(() -> {
      log.debug("타임아웃 콜백 호출, timerId: {}", timerId);
      emitter.complete();
    });
  }

}
