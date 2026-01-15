package com.sharetimer.syncservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties(prefix = "webclient.timer-api")
@RequiredArgsConstructor
@Getter
public class WebClientProps {

  private final String url;
  
}
