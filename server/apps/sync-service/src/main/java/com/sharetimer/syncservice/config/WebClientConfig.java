package com.sharetimer.syncservice.config;

import java.util.Objects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Bean("timerWebClient")
  public WebClient webClient(WebClientProps webClientProps) {
    return WebClient.builder().baseUrl(Objects.requireNonNull(webClientProps.getUrl()))
        .build();
  }

}
