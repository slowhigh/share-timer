package com.sharetimer.syncservice.adapter.in.listener;

import java.util.Objects;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharetimer.core.common.config.RedisMessageListenerContainerFactory;
import com.sharetimer.core.common.config.TimerProps;
import com.sharetimer.syncservice.adapter.in.listener.message.TimerUpdateTargetTimeMessage;
import com.sharetimer.syncservice.application.port.in.TimerUseCase;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimerTargetTimeUpdateListener implements MessageListener {

  private final TimerUseCase timerUseCase;
  private final ObjectMapper objectMapper;
  private final TimerProps timerProps;
  private final RedisMessageListenerContainerFactory factory;
  private final Environment env;

  @PostConstruct
  public void init() {
    String[] profiles = env.getActiveProfiles();
    String profile = (profiles != null && profiles.length > 0) ? profiles[0] : "local";

    String topic = Objects.requireNonNull(
        String.format("%s:%s", profile, timerProps.getPubSub().getTargetTimeUpdatedChannel()));

    log.debug("subscribe topic: {}", topic);

    factory.getContainer(timerProps.getPubSub().getDbIndex()).addMessageListener(this,
        new PatternTopic(topic));
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String body = new String(message.getBody());

    TimerUpdateTargetTimeMessage timerUpdateTargetTimeMessage = null;

    try {
      timerUpdateTargetTimeMessage =
          objectMapper.readValue(body, TimerUpdateTargetTimeMessage.class);
    } catch (Exception e) {
      log.error("메시지 파싱 실패 body: {}", body);
      return;
    }

    log.debug("onMessage: {}", timerUpdateTargetTimeMessage);

    timerUseCase.updateTargetTime(timerUpdateTargetTimeMessage.timerId(),
        timerUpdateTargetTimeMessage.payload().updatedAt(),
        timerUpdateTargetTimeMessage.payload().serverTime(),
        timerUpdateTargetTimeMessage.payload().newTargetTime());
  }

}
