package com.sharetimer.storage.redis.config;

import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisTemplateFactory {

  private final RedisProps redisProps;
  private volatile StringRedisTemplate template;

  public StringRedisTemplate getTemplate() {
    if (template == null) {
      synchronized (this) {
        if (template == null) {
          template = createTemplate();
        }
      }
    }
    return template;
  }

  private StringRedisTemplate createTemplate() {
    RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisProps.getHost(),
        redisProps.getPort());
    // Default DB 0

    LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig);
    factory.afterPropertiesSet();

    StringRedisTemplate template = new StringRedisTemplate(factory);
    template.afterPropertiesSet();
    return template;
  }
}
