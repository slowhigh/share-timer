package com.sharetimer.apiservice.adapter.input.web

import com.sharetimer.apiservice.adapter.input.web.dto.TimerAddTimestampReq
import com.sharetimer.apiservice.adapter.input.web.dto.TimerCreateReq
import com.sharetimer.apiservice.adapter.input.web.dto.TimerCreateRes
import com.sharetimer.apiservice.adapter.input.web.dto.TimerInfoRes
import com.sharetimer.apiservice.adapter.input.web.dto.TimerUpdateReq
import com.sharetimer.apiservice.application.port.input.TimerUseCase
import com.sharetimer.apiservice.application.port.input.command.AddTimestampCommand
import com.sharetimer.apiservice.application.port.input.command.CreateTimerCommand
import com.sharetimer.apiservice.application.port.input.command.DeleteTimerCommand
import com.sharetimer.apiservice.application.port.input.command.GetTimerQuery
import com.sharetimer.apiservice.application.port.input.command.UpdateTimerCommand
import com.sharetimer.web.support.dto.BaseRes
import com.sharetimer.web.support.dto.ErrorRes
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Clock
import java.util.UUID

@RestController
@RequestMapping("/timers")
class TimerController(
    private val timerUseCase: TimerUseCase,
    private val clock: Clock,
) {
    companion object {
        private val log = LoggerFactory.getLogger(TimerController::class.java)
    }

    @PostMapping
    @Operation(
        operationId = "createTimer",
        summary = "Create Timer",
        description = "Create a new timer.",
        tags = ["Timer"],
    )
    @ApiResponse(responseCode = "201", description = "Timer creation successful")
    @ApiResponse(
        responseCode = "400",
        description = "Timer creation failed",
        content = [Content(schema = Schema(implementation = ErrorRes::class))],
    )
    fun createTimer(
        @RequestBody @Valid timerCreateReq: TimerCreateReq,
    ): ResponseEntity<BaseRes<TimerCreateRes>> {
        log.debug("Timer creation requested: {}", timerCreateReq)

        val cmd = CreateTimerCommand(checkNotNull(timerCreateReq.targetTime))
        val timer = timerUseCase.createTimer(cmd)
        val res = TimerCreateRes.from(timer)

        log.debug("Timer creation response: {}", res)
        return BaseRes.of(HttpStatus.CREATED, res)
    }

    @GetMapping("/{timerId}")
    @Operation(
        operationId = "getTimerInfo",
        summary = "Get Timer Info",
        description = "Retrieve timer information.",
        tags = ["Timer"],
    )
    @ApiResponse(responseCode = "200", description = "Timer info retrieval successful")
    @ApiResponse(
        responseCode = "404",
        description = "Timer info retrieval failed",
        content = [Content(schema = Schema(implementation = ErrorRes::class))],
    )
    fun getTimerInfo(
        @PathVariable timerId: String,
        @RequestHeader(name = "\${timer.web.owner-token-header}", required = false)
        ownerToken: String?,
    ): ResponseEntity<BaseRes<TimerInfoRes>> {
        val timer = timerUseCase.getTimerInfo(GetTimerQuery(timerId))
        val isOwner = ownerToken != null && timer.ownerToken == UUID.fromString(ownerToken)
        val data = TimerInfoRes.from(clock.instant(), isOwner, timer)
        return BaseRes.of(HttpStatus.OK, data)
    }

    @DeleteMapping("/{timerId}")
    @Operation(
        operationId = "deleteTimer",
        summary = "Delete Timer",
        description = "Delete a timer.",
        tags = ["Timer"],
    )
    @ApiResponse(responseCode = "204", description = "Timer deletion successful")
    @ApiResponse(
        responseCode = "404",
        description = "Timer deletion failed",
        content = [Content(schema = Schema(implementation = ErrorRes::class))],
    )
    fun deleteTimer(
        @PathVariable timerId: String,
    ): ResponseEntity<Void> {
        timerUseCase.deleteTimer(DeleteTimerCommand(timerId))
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{timerId}")
    @Operation(
        operationId = "updateTimer",
        summary = "Update Timer",
        description = "Update a timer.",
        tags = ["Timer"],
    )
    @ApiResponse(responseCode = "202", description = "Timer update request successful")
    @ApiResponse(
        responseCode = "400",
        description = "Timer update request failed",
        content = [Content(schema = Schema(implementation = ErrorRes::class))],
    )
    @ApiResponse(
        responseCode = "403",
        description = "No permission to update timer",
        content = [Content(schema = Schema(implementation = ErrorRes::class))],
    )
    @ApiResponse(
        responseCode = "404",
        description = "Timer not found or expired",
        content = [Content(schema = Schema(implementation = ErrorRes::class))],
    )
    fun updateTimer(
        @PathVariable timerId: String,
        @RequestHeader(name = "\${timer.web.owner-token-header}") ownerToken: String,
        @RequestBody @Valid timerUpdateReq: TimerUpdateReq,
    ): ResponseEntity<BaseRes<Any?>> {
        timerUseCase.updateTimer(
            UpdateTimerCommand(
                timerId,
                ownerToken,
                checkNotNull(timerUpdateReq.requestTime),
                checkNotNull(timerUpdateReq.targetTime),
            ),
        )
        return BaseRes.of(HttpStatus.ACCEPTED, null)
    }

    @PostMapping("/{timerId}/timestamps")
    @Operation(
        operationId = "addTimestamp",
        summary = "Add Timestamp",
        description = "Add a timestamp.",
        tags = ["Timer"],
    )
    @ApiResponse(responseCode = "202", description = "Timestamp addition request successful")
    @ApiResponse(
        responseCode = "400",
        description = "Timestamp addition request failed",
        content = [Content(schema = Schema(implementation = ErrorRes::class))],
    )
    @ApiResponse(
        responseCode = "404",
        description = "Timer not found or expired",
        content = [Content(schema = Schema(implementation = ErrorRes::class))],
    )
    fun addTimestamp(
        @PathVariable timerId: String,
        @RequestBody @Valid timerAddTimestampReq: TimerAddTimestampReq,
    ): ResponseEntity<BaseRes<Any?>> {
        timerUseCase.addTimestamp(
            AddTimestampCommand(timerId, checkNotNull(timerAddTimestampReq.capturedAt)),
        )
        return BaseRes.of(HttpStatus.ACCEPTED, null)
    }
}
