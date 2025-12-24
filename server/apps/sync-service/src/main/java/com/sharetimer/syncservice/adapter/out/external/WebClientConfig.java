package com.sharetimer.syncservice.adapter.out.external;

import java.util.Objects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import com.sharetimer.core.common.config.WebClientProps;

@Configuration
public class WebClientConfig {

  @Bean("timerWebClient")
  public WebClient webClient(WebClientProps webClientProps) {
    return WebClient.builder().baseUrl(Objects.requireNonNull(webClientProps.getTimerApiUrl()))
        .build();
  }

}
