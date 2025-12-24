package com.sharetimer.core.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties(prefix = "webclient")
@Validated
@Getter
@RequiredArgsConstructor
public class WebClientProps {

  @NotBlank
  private final String timerApiUrl;

}
