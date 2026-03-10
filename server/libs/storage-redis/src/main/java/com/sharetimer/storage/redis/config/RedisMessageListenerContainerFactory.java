package com.sharetimer.storage.redis.config;

import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisMessageListenerContainerFactory {

  private final RedisProps redisProps;
  private volatile RedisMessageListenerContainer container;

  public RedisMessageListenerContainer getContainer() {
    if (container == null) {
      synchronized (this) {
        if (container == null) {
          container = createContainer();
        }
      }
    }
    return container;
  }

  private RedisMessageListenerContainer createContainer() {
    RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisProps.getHost(),
        redisProps.getPort());

    LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig);
    factory.afterPropertiesSet();
    factory.getConnection().serverCommands().setConfig("notify-keyspace-events", "Ex");

    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(factory);
    container.afterPropertiesSet();
    container.start();
    return container;
  }
}
