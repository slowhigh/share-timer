package com.sharetimer.syncservice.adapter.out.external;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TimerApiClient {

  @Qualifier("timerWebClient")
  private final WebClient webClient;

  public Mono<Void> deleteTimer(String timerId) {
    return webClient.delete().uri("/timers/" + timerId).retrieve().bodyToMono(Void.class);
  }

}
