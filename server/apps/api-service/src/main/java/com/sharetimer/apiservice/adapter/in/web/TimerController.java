package com.sharetimer.apiservice.adapter.in.web;

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
import com.sharetimer.apiservice.adapter.in.web.dto.TimerAddTimestampReq;
import com.sharetimer.apiservice.adapter.in.web.dto.TimerCreateReq;
import com.sharetimer.apiservice.adapter.in.web.dto.TimerCreateRes;
import com.sharetimer.apiservice.adapter.in.web.dto.TimerInfoRes;
import com.sharetimer.apiservice.adapter.in.web.dto.TimerUpdateReq;
import com.sharetimer.apiservice.application.port.in.TimerUseCase;
import com.sharetimer.apiservice.application.port.in.command.AddTimestampCommand;
import com.sharetimer.apiservice.application.port.in.command.CreateTimerCommand;
import com.sharetimer.apiservice.application.port.in.command.DeleteTimerCommand;
import com.sharetimer.apiservice.application.port.in.command.GetTimerCommand;
import com.sharetimer.apiservice.application.port.in.command.UpdateTimerCommand;
import com.sharetimer.web.support.dto.BaseRes;
import com.sharetimer.web.support.dto.ErrorRes;
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

  private final TimerUseCase timerUseCase;

  @PostMapping
  @Operation(operationId = "createTimer", summary = "Create Timer",
      description = "Create a new timer.", tags = {"Timer"})
  @ApiResponse(responseCode = "201", description = "Timer creation successful")
  @ApiResponse(responseCode = "400", description = "Timer creation failed",
      content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
  public ResponseEntity<BaseRes<TimerCreateRes>> createTimer(
      @RequestBody @Valid TimerCreateReq timerCreateReq) {
    log.debug("Timer creation requested: {}", timerCreateReq);

    CreateTimerCommand command = new CreateTimerCommand(timerCreateReq.targetTime());
    TimerCreateRes data = timerUseCase.createTimer(command);

    log.debug("Timer creation response: {}", data);
    return BaseRes.of(HttpStatus.CREATED, data);
  }

  @GetMapping("/{timerId}")
  @Operation(operationId = "getTimerInfo", summary = "Get Timer Info",
      description = "Retrieve timer information.", tags = {"Timer"})
  @ApiResponse(responseCode = "200", description = "Timer info retrieval successful")
  @ApiResponse(responseCode = "404", description = "Timer info retrieval failed",
      content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
  public ResponseEntity<BaseRes<TimerInfoRes>> getTimerInfo(@PathVariable String timerId,
      @RequestHeader(name = "${timer.web.owner-token-header}") @Nullable String ownerToken) {
    GetTimerCommand command = new GetTimerCommand(timerId, ownerToken);
    TimerInfoRes data = timerUseCase.getTimerInfo(command);
    return BaseRes.of(HttpStatus.OK, data);
  }

  @DeleteMapping("/{timerId}")
  @Operation(operationId = "deleteTimer", summary = "Delete Timer", description = "Delete a timer.",
      tags = {"Timer"})
  @ApiResponse(responseCode = "204", description = "Timer deletion successful")
  @ApiResponse(responseCode = "404", description = "Timer deletion failed",
      content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
  public ResponseEntity<Void> deleteTimer(@PathVariable String timerId) {
    timerUseCase.deleteTimer(new DeleteTimerCommand(timerId));
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{timerId}")
  @Operation(operationId = "updateTimer", summary = "Update Timer", description = "Update a timer.",
      tags = {"Timer"})
  @ApiResponse(responseCode = "202", description = "Timer update request successful")
  @ApiResponse(responseCode = "400", description = "Timer update request failed",
      content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
  @ApiResponse(responseCode = "403", description = "No permission to update timer",
      content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
  @ApiResponse(responseCode = "404", description = "Timer not found or expired",
      content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
  public ResponseEntity<BaseRes<Void>> updateTimer(@PathVariable String timerId,
      @RequestHeader(name = "${timer.web.owner-token-header}") String ownerToken,
      @RequestBody @Valid TimerUpdateReq timerUpdateReq) {
    timerUseCase.updateTimer(new UpdateTimerCommand(timerId, ownerToken,
        timerUpdateReq.requestTime(), timerUpdateReq.targetTime()));
    return BaseRes.of(HttpStatus.ACCEPTED, null);
  }

  @PostMapping("/{timerId}/timestamps")
  @Operation(operationId = "addTimestamp", summary = "Add Timestamp",
      description = "Add a timestamp.", tags = {"Timer"})
  @ApiResponse(responseCode = "202", description = "Timestamp addition request successful")
  @ApiResponse(responseCode = "400", description = "Timestamp addition request failed",
      content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
  @ApiResponse(responseCode = "404", description = "Timer not found or expired",
      content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
  public ResponseEntity<BaseRes<Void>> addTimestamp(@PathVariable String timerId,
      @RequestBody @Valid TimerAddTimestampReq timerAddTimestampReq) {
    timerUseCase.addTimestamp(new AddTimestampCommand(timerId, timerAddTimestampReq.capturedAt()));
    return BaseRes.of(HttpStatus.ACCEPTED, null);
  }
}
