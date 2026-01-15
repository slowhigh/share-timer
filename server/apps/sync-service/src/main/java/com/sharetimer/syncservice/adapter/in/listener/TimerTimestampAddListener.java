package com.sharetimer.syncservice.adapter.in.listener;

import java.util.Objects;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharetimer.storage.redis.config.RedisMessageListenerContainerFactory;
import com.sharetimer.storage.redis.config.TimerRedisProps;
import com.sharetimer.syncservice.adapter.in.listener.message.TimerAddTimestampMessage;
import com.sharetimer.syncservice.application.port.in.TimerUseCase;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimerTimestampAddListener implements MessageListener {

  private final TimerUseCase timerUseCase;
  private final ObjectMapper objectMapper;
  private final TimerRedisProps timerRedisProps;
  private final RedisMessageListenerContainerFactory factory;
  private final Environment env;

  @PostConstruct
  public void init() {
    String[] profiles = env.getActiveProfiles();
    String profile = (profiles != null && profiles.length > 0) ? profiles[0] : "local";

    String topic = Objects.requireNonNull(
        String.format("%s:%s", profile, timerRedisProps.getPubSub().getTimestampAddedChannel()));

    log.debug("subscribe topic: {}", topic);

    factory.getContainer(timerRedisProps.getPubSub().getDbIndex()).addMessageListener(this,
        new PatternTopic(topic));
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String body = new String(message.getBody());

    TimerAddTimestampMessage timerAddTimestampMessage = null;

    try {
      timerAddTimestampMessage = objectMapper.readValue(body, TimerAddTimestampMessage.class);
    } catch (Exception e) {
      log.error("Message parsing failed body: {}", body);
      return;
    }

    log.debug("onMessage: {}", timerAddTimestampMessage);

    timerUseCase.addTimestamp(timerAddTimestampMessage.timerId(),
        timerAddTimestampMessage.payload().targetTime(),
        timerAddTimestampMessage.payload().capturedAt());
  }
}
