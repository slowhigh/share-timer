package com.sharetimer.storage.redis.config;

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
  private volatile ReactiveRedisTemplate<String, String> template;

  public ReactiveRedisTemplate<String, String> getTemplate() {
    if (template == null) {
      synchronized (this) {
        if (template == null) {
          template = createTemplate();
        }
      }
    }
    return template;
  }

  private ReactiveRedisTemplate<String, String> createTemplate() {
    RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisProps.getHost(),
        redisProps.getPort());

    LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig);
    factory.afterPropertiesSet();

    RedisSerializationContext<String, String> serializationContext = RedisSerializationContext
        .<String, String>newSerializationContext(new StringRedisSerializer())
        .key(new StringRedisSerializer()).value(new StringRedisSerializer())
        .hashKey(new StringRedisSerializer()).hashValue(new StringRedisSerializer()).build();

    return new ReactiveRedisTemplate<>(factory, serializationContext);
  }
}
