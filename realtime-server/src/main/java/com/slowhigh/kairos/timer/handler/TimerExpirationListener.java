package com.slowhigh.kairos.timer.handler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import com.slowhigh.kairos.timer.config.TimerProps;
import com.slowhigh.kairos.timer.service.TimerService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TimerExpirationListener extends KeyExpirationEventMessageListener {

  private final TimerService timerService;
  private final TimerProps timerProps;

  public TimerExpirationListener(
      @Qualifier("timerExpirationRedisListenerContainer") RedisMessageListenerContainer redisMessageListenerContainer,
      TimerService timerService, TimerProps timerProps) {
    super(redisMessageListenerContainer);
    this.timerService = timerService;
    this.timerProps = timerProps;

    String topic =
        String.format("__keyevent@%d__:expired", timerProps.getExpiration().getDbIndex());
    redisMessageListenerContainer.addMessageListener(this, new PatternTopic(topic));
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String body = new String(message.getBody());
    log.info("Expired key: {}", body);

    if (body.startsWith(timerProps.getExpiration().getKeyPrefix())) {
      String timerId = body.substring(timerProps.getExpiration().getKeyPrefix().length());
      timerService.processTimerExpiration(timerId);
    }
  }
}
