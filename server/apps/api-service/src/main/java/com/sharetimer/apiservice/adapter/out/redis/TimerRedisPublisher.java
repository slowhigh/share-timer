package com.sharetimer.apiservice.adapter.out.redis;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharetimer.apiservice.adapter.out.redis.message.TimerAddTimestampMessage;
import com.sharetimer.apiservice.adapter.out.redis.message.TimerUpdateTargetTimeMessage;
import com.sharetimer.apiservice.application.port.out.TimerEventPort;
import com.sharetimer.core.common.config.TimerProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimerRedisPublisher implements TimerEventPort {

  private final TimerProps timerProps;
  private final ObjectMapper objectMapper;
  private final Environment env;

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

    publishEvent(channel, message);
  }

  public void publishAddTimestamp(String timerId, Instant targetTime, Instant capturedAt) {
    String channel = timerProps.getPubSub().getTimestampAddedChannel();

    TimerAddTimestampMessage message =
        new TimerAddTimestampMessage(timerId, timerProps.getPubSub().getTimestampAddedMessageType(),
            new TimerAddTimestampMessage.Payload(targetTime, capturedAt));

    publishEvent(channel, message);
  }

  private void publishEvent(String channel, Object message) {
    try {
      String jsonMessage = objectMapper.writeValueAsString(message);
      if (jsonMessage == null) {
        log.error("메시지 직렬화 실패: 메시지가 null입니다.");
        return;
      }
      String fullChannel = String.format("%s:%s", env.getActiveProfiles()[0], channel);
      if (fullChannel == null) {
        log.error("채널 이름이 null입니다.");
        return;
      }

      timerPubSubRedisTemplate.convertAndSend(fullChannel, jsonMessage);
      log.debug("Redis publish 성공: channel={}, message={}", fullChannel, jsonMessage);
    } catch (JsonProcessingException e) {
      log.error("메시지 직렬화 실패: {}", e.getMessage());
    } catch (Exception e) {
      log.error("Redis publish 실패: channel={}, error={}", channel, e.getMessage());
    }
  }

}
