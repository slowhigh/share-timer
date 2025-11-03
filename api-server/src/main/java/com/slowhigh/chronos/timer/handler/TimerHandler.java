package com.slowhigh.chronos.timer.handler;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slowhigh.chronos.timer.config.TimerProps;
import com.slowhigh.chronos.timer.handler.message.TimerAddTimestampMessage;
import com.slowhigh.chronos.timer.handler.message.TimerUpdateTargetTimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimerHandler {

  private final TimerProps timerProps;
  private final ObjectMapper objectMapper;

  @Qualifier("timerExpirationRedisTemplate")
  private final StringRedisTemplate timerExpirationRedisTemplate;

  @Qualifier("timerPubSubRedisTemplate")
  private final StringRedisTemplate timerPubSubRedisTemplate;

  public void scheduleExpiration(String timerId, Instant targetTime) {
    long expireSecond = targetTime.getEpochSecond() - Instant.now().getEpochSecond();
    if (expireSecond > 0) {
      String keyPrefix = timerProps.getExpiration().getKeyPrefix();
      String key = keyPrefix + timerId;
      timerExpirationRedisTemplate.opsForValue().set(key, "", expireSecond, TimeUnit.SECONDS);
      log.debug("Timer({})가 {}초 후에 만료되도록 설정되었습니다.", timerId, expireSecond);
    }
  }

  public void publishUpdateTimerTargetTime(String timerId, Instant updatedAt, Instant targetTime) {
    String channel = timerProps.getPubSub().getTargetTimeUpdatedChannel();

    TimerUpdateTargetTimeMessage message = new TimerUpdateTargetTimeMessage(timerId,
        timerProps.getPubSub().getTargetTimeUpdatedMessageType(),
        new TimerUpdateTargetTimeMessage.Payload(updatedAt, Instant.now(), targetTime));

    try {
      String jsonMessage = objectMapper.writeValueAsString(message);
      timerPubSubRedisTemplate.convertAndSend(channel, jsonMessage);
      log.debug("Timer({})의 타겟 시간이 업데이트되었습니다. 푸시 메시지 전송: {}", timerId, jsonMessage);
    } catch (Exception e) {
      log.error("Timer({})의 타겟 시간 업데이트 푸시 메시지 전송 실패: {}", timerId, e.getMessage());
    }
  }

  public void publishAddTimestamp(String timerId, Instant targetTime, Instant capturedAt) {
    String channel = timerProps.getPubSub().getTimestampAddedChannel();

    TimerAddTimestampMessage message =
        new TimerAddTimestampMessage(timerId, timerProps.getPubSub().getTimestampAddedMessageType(),
            new TimerAddTimestampMessage.Payload(targetTime, capturedAt));

    try {
      String jsonMessage = objectMapper.writeValueAsString(message);
      timerPubSubRedisTemplate.convertAndSend(channel, jsonMessage);
      log.debug("Timer({})에 타임스탬프가 추가되었습니다. 푸시 메시지 전송: {}", timerId, jsonMessage);
    } catch (Exception e) {
      log.error("Timer({})에 타임스탬프 추가 푸시 메시지 전송 실패: {}", timerId, e.getMessage());
    }
  }


}
