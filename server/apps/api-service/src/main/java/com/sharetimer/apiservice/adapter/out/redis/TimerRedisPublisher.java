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
import com.sharetimer.storage.redis.config.TimerRedisProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimerRedisPublisher implements TimerEventPort {

  private final TimerRedisProps timerRedisProps;
  private final ObjectMapper objectMapper;
  private final Environment env;

  @Qualifier("timerExpirationRedisTemplate")
  private final StringRedisTemplate timerExpirationRedisTemplate;

  @Qualifier("timerPubSubRedisTemplate")
  private final StringRedisTemplate timerPubSubRedisTemplate;

  public void scheduleExpiration(String timerId, Instant targetTime) {
    long expireSecond = targetTime.getEpochSecond() - Instant.now().getEpochSecond();
    if (expireSecond > 0) {
      String keyPrefix = timerRedisProps.getExpiration().getKeyPrefix();
      String key = keyPrefix + timerId;
      timerExpirationRedisTemplate.opsForValue().set(key, "", expireSecond, TimeUnit.SECONDS);
      log.debug("Timer({}) is set to expire in {} seconds.", timerId, expireSecond);
    }
  }

  public void publishUpdateTimerTargetTime(String timerId, Instant updatedAt, Instant targetTime) {
    String channel = timerRedisProps.getPubSub().getTargetTimeUpdatedChannel();

    TimerUpdateTargetTimeMessage message = new TimerUpdateTargetTimeMessage(timerId,
        timerRedisProps.getPubSub().getTargetTimeUpdatedMessageType(),
        new TimerUpdateTargetTimeMessage.Payload(updatedAt, Instant.now(), targetTime));

    publishEvent(channel, message);
  }

  public void publishAddTimestamp(String timerId, Instant targetTime, Instant capturedAt) {
    String channel = timerRedisProps.getPubSub().getTimestampAddedChannel();

    TimerAddTimestampMessage message = new TimerAddTimestampMessage(timerId,
        timerRedisProps.getPubSub().getTimestampAddedMessageType(),
        new TimerAddTimestampMessage.Payload(targetTime, capturedAt));

    publishEvent(channel, message);
  }

  private void publishEvent(String channel, Object message) {
    try {
      String jsonMessage = objectMapper.writeValueAsString(message);
      if (jsonMessage == null) {
        log.error("Message serialization failed: Message is null.");
        return;
      }
      String fullChannel = String.format("%s:%s", env.getActiveProfiles()[0], channel);
      if (fullChannel == null) {
        log.error("Channel name is null.");
        return;
      }

      timerPubSubRedisTemplate.convertAndSend(fullChannel, jsonMessage);
      log.debug("Redis publish success: channel={}, message={}", fullChannel, jsonMessage);
    } catch (JsonProcessingException e) {
      log.error("Message serialization failed: {}", e.getMessage());
    } catch (Exception e) {
      log.error("Redis publish failed: channel={}, error={}", channel, e.getMessage());
    }
  }

}
