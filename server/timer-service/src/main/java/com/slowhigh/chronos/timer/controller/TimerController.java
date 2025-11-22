package com.slowhigh.chronos.timer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.slowhigh.chronos.common.dto.BaseRes;
import com.slowhigh.chronos.common.exception.ErrorRes;
import com.slowhigh.chronos.timer.dto.TimerAddTimestampReq;
import com.slowhigh.chronos.timer.dto.TimerCreateReq;
import com.slowhigh.chronos.timer.dto.TimerCreateRes;
import com.slowhigh.chronos.timer.dto.TimerInfoRes;
import com.slowhigh.chronos.timer.dto.TimerUpdateReq;
import com.slowhigh.chronos.timer.service.TimerService;
import com.slowhigh.chronos.timer.service.command.AddTimestampCommand;
import com.slowhigh.chronos.timer.service.command.CreateTimerCommand;
import com.slowhigh.chronos.timer.service.command.DeleteTimerCommand;
import com.slowhigh.chronos.timer.service.command.GetTimerCommand;
import com.slowhigh.chronos.timer.service.command.UpdateTimerCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/timers")
@RequiredArgsConstructor
@Slf4j
public class TimerController {

  private final TimerService timerService;

  @PostMapping
  @Operation(operationId = "createTimer", summary = "타이머 생성", description = "새로운 타이머를 생성합니다.",
      tags = {"Timer"})
  @ApiResponse(responseCode = "201", description = "타이머 생성 성공")
  @ApiResponse(responseCode = "400", description = "타이머 생성 실패",
      content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
  public ResponseEntity<BaseRes<TimerCreateRes>> createTimer(
      @RequestBody @Valid TimerCreateReq timerCreateReq) {
    log.debug("타이머 생성 요청: {}", timerCreateReq);

    CreateTimerCommand command = new CreateTimerCommand(timerCreateReq.targetTime());
    TimerCreateRes data = timerService.createTimer(command);

    log.debug("타이머 생성 응답: {}", data);
    return BaseRes.of(HttpStatus.CREATED, data);
  }

  @GetMapping("/{timerId}")
  @Operation(operationId = "getTimerInfo", summary = "타이머 정보 조회", description = "타이머 정보를 조회합니다.",
      tags = {"Timer"})
  @ApiResponse(responseCode = "200", description = "타이머 정보 조회 성공")
  @ApiResponse(responseCode = "404", description = "타이머 정보 조회 실패",
      content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
  public ResponseEntity<BaseRes<TimerInfoRes>> getTimerInfo(
      @PathVariable(name = "timerId") String timerId,
      @RequestHeader(name = "${timer.owner-token-header}") @Nullable String ownerToken) {
    GetTimerCommand command = new GetTimerCommand(timerId, ownerToken);
    TimerInfoRes data = timerService.getTimerInfo(command);
    return BaseRes.of(HttpStatus.OK, data);
  }

  @DeleteMapping("/{timerId}")
  @Operation(operationId = "deleteTimer", summary = "타이머 삭제", description = "타이머를 삭제합니다.",
      tags = {"Timer"})
  @ApiResponse(responseCode = "204", description = "타이머 삭제 성공")
  @ApiResponse(responseCode = "404", description = "타이머 삭제 실패",
      content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
  public ResponseEntity<Void> deleteTimer(@PathVariable(name = "timerId") String timerId) {
    timerService.deleteTimer(new DeleteTimerCommand(timerId));
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{timerId}")
  @Operation(operationId = "updateTimer", summary = "타이머 업데이트", description = "타이머를 업데이트합니다.",
      tags = {"Timer"})
  @ApiResponse(responseCode = "202", description = "타이머 업데이트 요청 성공")
  @ApiResponse(responseCode = "400", description = "타이머 업데이트 요청 실패",
      content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
  @ApiResponse(responseCode = "403", description = "타이머 업데이트 권한이 없음",
      content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
  @ApiResponse(responseCode = "404", description = "타이머가 없거나 종료됨",
      content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
  public ResponseEntity<BaseRes<Void>> updateTimer(@PathVariable(name = "timerId") String timerId,
      @RequestHeader(name = "${timer.owner-token-header}") String ownerToken,
      @RequestBody @Valid TimerUpdateReq timerUpdateReq) {
    timerService.updateTimer(new UpdateTimerCommand(timerId, ownerToken,
        timerUpdateReq.requestTime(), timerUpdateReq.targetTime()));
    return BaseRes.of(HttpStatus.ACCEPTED, null);
  }

  @PostMapping("/{timerId}/timestamps")
  @Operation(operationId = "addTimestamp", summary = "타임스탬프 추가", description = "타임스탬프를 추가합니다.",
      tags = {"Timer"})
  @ApiResponse(responseCode = "202", description = "타임스탬프 추가 요청 성공")
  @ApiResponse(responseCode = "400", description = "타임스탬프 추가 요청 실패",
      content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
  @ApiResponse(responseCode = "404", description = "타이머가 없거나 종료됨",
      content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
  public ResponseEntity<BaseRes<Void>> addTimestamp(@PathVariable(name = "timerId") String timerId,
      @RequestBody @Valid TimerAddTimestampReq timerAddTimestampReq) {
    timerService.addTimestamp(new AddTimestampCommand(timerId, timerAddTimestampReq.capturedAt()));
    return BaseRes.of(HttpStatus.ACCEPTED, null);
  }
}
