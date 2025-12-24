package com.sharetimer.syncservice.adapter.in.web;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.sharetimer.syncservice.application.port.in.TimerUseCase;
import reactor.core.publisher.Flux;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("local")
class TimerControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private TimerUseCase timerUseCase;

  @Test
  void subscribeEvents_ShouldReturnSseStream() {
    String timerId = "test-timer";
    Flux<ServerSentEvent<Object>> eventStream =
        Flux.just(ServerSentEvent.builder().event("connect").data("connected").build(),
            ServerSentEvent.builder().event("heartbeat").build());

    given(timerUseCase.subscribe(timerId)).willReturn(eventStream);

    webTestClient.get().uri("/timers/{timerId}", timerId).accept(MediaType.TEXT_EVENT_STREAM)
        .exchange().expectStatus().isOk().expectHeader()
        .contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM);

    verify(timerUseCase).subscribe(timerId);
  }
}
