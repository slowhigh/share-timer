package com.sharetimer.syncservice.adapter.in.web;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
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

@RestController
@RequestMapping("/timers")
@RequiredArgsConstructor
public class TimerController {

  private final TimerUseCase timerUseCase;

  @GetMapping(value = "/{timerId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @Operation(operationId = "subscribeEvents", summary = "타이머 이벤트 구독",
      description = "타이머 관련 이벤트를 SSE로 구독합니다.", tags = {"Timer"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = """
      타이머 구독 성공. SSE 스트림을 통해 아래 3가지 종류의 이벤트가 전송될 수 있습니다.
      각 이벤트의 `data` 필드는 아래 스키마 중 하나를 따릅니다.
      - `event: targetTimeUpdate`: 기준 시간 변경
      - `event: timerEnd`: 타이머 종료 (데이터 없음)
      - `event: timestampAdded`: Timestamp 추가
      - `event: timerNotFound`: 타이머가 존재하지 않음
      """, content = @Content(mediaType = MediaType.TEXT_EVENT_STREAM_VALUE, schema = @Schema(
      oneOf = {TargetTimeUpdateEvent.class, TimerEndEvent.class, TimestampAddedEvent.class}))),})
  public ResponseEntity<SseEmitter> subscribeEvents(@PathVariable String timerId) {
    SseEmitter emitter = timerUseCase.subscribe(timerId);
    return ResponseEntity.ok(emitter);
  }
}
