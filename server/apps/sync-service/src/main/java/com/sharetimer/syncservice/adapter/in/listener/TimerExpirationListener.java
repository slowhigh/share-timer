package com.sharetimer.syncservice.adapter.in.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.stereotype.Component;
import com.sharetimer.storage.redis.config.RedisMessageListenerContainerFactory;
import com.sharetimer.storage.redis.config.TimerRedisProps;
import com.sharetimer.syncservice.application.port.in.TimerUseCase;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimerExpirationListener implements MessageListener {

  private final TimerUseCase timerUseCase;
  private final TimerRedisProps timerRedisProps;
  private final RedisMessageListenerContainerFactory factory;

  @PostConstruct
  public void init() {
    int idx = timerRedisProps.getExpiration().getDbIndex();
    factory.getContainer(idx).addMessageListener(this,
        new PatternTopic(String.format("__keyevent@%d__:expired", idx)));
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String body = new String(message.getBody());
    log.info("Expired key: {}", body);

    if (body.startsWith(timerRedisProps.getExpiration().getKeyPrefix())) {
      String timerId = body.substring(timerRedisProps.getExpiration().getKeyPrefix().length());
      timerUseCase.processTimerExpiration(timerId);
    }
  }
}
