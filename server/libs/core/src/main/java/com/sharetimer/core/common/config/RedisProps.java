package com.sharetimer.core.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties(prefix = "spring.data.redis")
@RequiredArgsConstructor
@Getter
public class RedisProps {

  private final String host;
  private final int port;

}
