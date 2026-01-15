package com.sharetimer.syncservice.adapter.in.web;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sharetimer.syncservice.adapter.out.web.dto.TargetTimeUpdateEvent;
import com.sharetimer.syncservice.adapter.out.web.dto.TimerEndEvent;
import com.sharetimer.syncservice.adapter.out.web.dto.TimestampAddedEvent;
import com.sharetimer.syncservice.application.port.in.TimerUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/timers")
@RequiredArgsConstructor
public class TimerController {

  private final TimerUseCase timerUseCase;

  @GetMapping(value = "/{timerId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @Operation(operationId = "subscribeEvents", summary = "Subscribe Timer Events",
      description = "Subscribe to timer-related events via SSE.", tags = {"Timer"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = """
      Timer subscription successful. 3 types of events can be sent via SSE stream.
      Each event's `data` field follows one of the schemas below.
      - `event: targetTimeUpdate`: Target time update
      - `event: timerEnd`: Timer ended (no data)
      - `event: timestampAdded`: Timestamp added
      - `event: timerNotFound`: Timer not found
      """, content = @Content(mediaType = MediaType.TEXT_EVENT_STREAM_VALUE, schema = @Schema(
      oneOf = {TargetTimeUpdateEvent.class, TimerEndEvent.class, TimestampAddedEvent.class}))),})
  public Flux<ServerSentEvent<Object>> subscribeEvents(@PathVariable String timerId) {
    return timerUseCase.subscribe(timerId);
  }
}
