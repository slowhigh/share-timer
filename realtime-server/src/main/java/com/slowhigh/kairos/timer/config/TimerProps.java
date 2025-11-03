package com.slowhigh.kairos.timer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties(prefix = "timer")
@RequiredArgsConstructor
@Getter
public class TimerProps {

  private final Expiration expiration;
  private final PubSub pubSub;

  @Getter
  @RequiredArgsConstructor
  public static class Expiration {
    private final int dbIndex;
    private final String keyPrefix;
  }

  @Getter
  @RequiredArgsConstructor
  public static class PubSub {
    private final int dbIndex;
    private final String targetTimeUpdatedChannel;
    private final String targetTimeUpdatedMessageType;
    private final String timestampAddedChannel;
    private final String timestampAddedMessageType;
  }
}
