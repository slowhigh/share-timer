package com.sharetimer.apiservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.sharetimer.core.common.config.RedisTemplateFactory;
import com.sharetimer.core.common.config.TimerProps;

@Configuration
public class TimerConfig {

  @Bean("timerExpirationRedisTemplate")
  public StringRedisTemplate timerExpirationRedisTemplate(RedisTemplateFactory redisTemplateFactory,
      TimerProps timerProps) {
    int dbIndex = timerProps.getExpiration().getDbIndex();
    return redisTemplateFactory.getTemplate(dbIndex);
  }

  @Bean("timerPubSubRedisTemplate")
  public StringRedisTemplate timerPubSubRedisTemplate(RedisTemplateFactory redisTemplateFactory,
      TimerProps timerProps) {
    int dbIndex = timerProps.getPubSub().getDbIndex();
    return redisTemplateFactory.getTemplate(dbIndex);
  }

}
