package com.sharetimer.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties(prefix = "info")
@RequiredArgsConstructor
@Getter
public class InfoProps {

  private final String environment;

}
