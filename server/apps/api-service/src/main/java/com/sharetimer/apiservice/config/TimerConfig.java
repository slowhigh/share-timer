package com.sharetimer.apiservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.sharetimer.storage.redis.config.RedisTemplateFactory;
import com.sharetimer.storage.redis.config.TimerRedisProps;

@Configuration
public class TimerConfig {

  @Bean("timerExpirationRedisTemplate")
  public StringRedisTemplate timerExpirationRedisTemplate(RedisTemplateFactory redisTemplateFactory,
      TimerRedisProps timerRedisProps) {
    int dbIndex = timerRedisProps.getExpiration().getDbIndex();
    return redisTemplateFactory.getTemplate(dbIndex);
  }

  @Bean("timerPubSubRedisTemplate")
  public StringRedisTemplate timerPubSubRedisTemplate(RedisTemplateFactory redisTemplateFactory,
      TimerRedisProps timerRedisProps) {
    int dbIndex = timerRedisProps.getPubSub().getDbIndex();
    return redisTemplateFactory.getTemplate(dbIndex);
  }

}
