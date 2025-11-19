package com.slowhigh.kairos.timer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import com.slowhigh.kairos.common.config.RedisMessageListenerContainerFactory;
import com.slowhigh.kairos.timer.handler.TimerTargetTimeUpdateListener;
import com.slowhigh.kairos.timer.handler.TimerTimestampAddListener;

@Configuration
public class TimerConfig {

  @Bean("timerExpirationRedisListenerContainer")
  public RedisMessageListenerContainer timerExpirationRedisListenerContainer(
      RedisMessageListenerContainerFactory redisMessageListenerContainerFactory,
      TimerProps timerProps) {

    return redisMessageListenerContainerFactory
        .getContainer(timerProps.getExpiration().getDbIndex());
  }

  @Bean("timerPubSubRedisListenerContainer")
  public RedisMessageListenerContainer timerPubSubRedisListenerContainer(
      RedisMessageListenerContainerFactory redisMessageListenerContainerFactory,
      TimerProps timerProps, TimerTargetTimeUpdateListener timerTargetTimeUpdateListener,
      TimerTimestampAddListener timerTimestampAddListener) {

    RedisMessageListenerContainer container =
        redisMessageListenerContainerFactory.getContainer(timerProps.getPubSub().getDbIndex());
    container.addMessageListener(timerTargetTimeUpdateListener,
        new PatternTopic(timerProps.getPubSub().getTargetTimeUpdatedChannel()));
    container.addMessageListener(timerTimestampAddListener,
        new PatternTopic(timerProps.getPubSub().getTimestampAddedChannel()));
    return container;
  }

}
