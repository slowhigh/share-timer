package com.slowhigh.chronos.common.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisTemplateFactory {

  private final RedisProps redisProps;
  private final Map<Integer, StringRedisTemplate> templates = new ConcurrentHashMap<>();

  public StringRedisTemplate getTemplate(int dbIndex) {
    if (dbIndex < 0 || dbIndex > 15) {
      throw new IllegalArgumentException("Redis DB index는 0부터 15까지 가능합니다.");
    }
    return templates.computeIfAbsent(dbIndex, this::createTemplateForDb);
  }

  private StringRedisTemplate createTemplateForDb(int dbIndex) {
    RedisStandaloneConfiguration redisConfig =
        new RedisStandaloneConfiguration(redisProps.getHost(), redisProps.getPort());
    redisConfig.setDatabase(dbIndex);

    LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig);
    factory.afterPropertiesSet();

    StringRedisTemplate template = new StringRedisTemplate(factory);
    template.afterPropertiesSet();
    return template;
  }
}
