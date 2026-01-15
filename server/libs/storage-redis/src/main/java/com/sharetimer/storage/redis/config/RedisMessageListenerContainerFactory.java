package com.sharetimer.storage.redis.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisMessageListenerContainerFactory {

  private final RedisProps redisProps;
  private final Map<Integer, RedisMessageListenerContainer> containers = new ConcurrentHashMap<>();

  public RedisMessageListenerContainer getContainer(int dbIndex) {
    if (dbIndex < 0 || dbIndex > 15) {
      throw new IllegalArgumentException("Redis DB index must be between 0 and 15.");
    }
    return containers.computeIfAbsent(dbIndex, this::createContainerForDb);
  }

  private RedisMessageListenerContainer createContainerForDb(int dbIndex) {
    RedisStandaloneConfiguration redisConfig =
        new RedisStandaloneConfiguration(redisProps.getHost(), redisProps.getPort());
    redisConfig.setDatabase(dbIndex);

    LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig);
    factory.afterPropertiesSet();

    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(factory);
    container.afterPropertiesSet();
    container.start();
    return container;
  }
}
