package com.sharetimer.web.support.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties(prefix = "timer.web")
@RequiredArgsConstructor
@Getter
public class TimerWebProps {

  private final String ownerTokenHeader;
  
}
