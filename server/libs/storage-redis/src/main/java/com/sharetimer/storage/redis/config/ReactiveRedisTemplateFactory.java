package com.sharetimer.storage.redis.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReactiveRedisTemplateFactory {

  private final RedisProps redisProps;
  private final Map<Integer, ReactiveRedisTemplate<String, String>> templates =
      new ConcurrentHashMap<>();

  public ReactiveRedisTemplate<String, String> getTemplate(int dbIndex) {
    if (dbIndex < 0 || dbIndex > 15) {
      throw new IllegalArgumentException("Redis DB index must be between 0 and 15.");
    }
    return templates.computeIfAbsent(dbIndex, this::createTemplateForDb);
  }

  private ReactiveRedisTemplate<String, String> createTemplateForDb(int dbIndex) {
    RedisStandaloneConfiguration redisConfig =
        new RedisStandaloneConfiguration(redisProps.getHost(), redisProps.getPort());
    redisConfig.setDatabase(dbIndex);

    LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig);
    factory.afterPropertiesSet();

    RedisSerializationContext<String, String> serializationContext = RedisSerializationContext
        .<String, String>newSerializationContext(new StringRedisSerializer())
        .key(new StringRedisSerializer()).value(new StringRedisSerializer())
        .hashKey(new StringRedisSerializer()).hashValue(new StringRedisSerializer()).build();

    return new ReactiveRedisTemplate<>(factory, serializationContext);
  }
}
